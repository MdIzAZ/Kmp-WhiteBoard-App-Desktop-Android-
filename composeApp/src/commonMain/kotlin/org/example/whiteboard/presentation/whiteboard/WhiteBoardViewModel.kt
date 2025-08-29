package org.example.whiteboard.presentation.whiteboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.example.whiteboard.Information
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.DrawingTool
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.domain.model.Mode
import org.example.whiteboard.domain.model.Whiteboard
import org.example.whiteboard.domain.repo.PathRepo
import org.example.whiteboard.domain.repo.RemoteDbRepo
import org.example.whiteboard.domain.repo.RoomRepo
import org.example.whiteboard.domain.repo.SettingsRepo
import org.example.whiteboard.domain.repo.WhiteboardRepo
import org.example.whiteboard.presentation.navigation.Routes
import java.util.UUID

class WhiteBoardViewModel(
    private val pathRepo: PathRepo,
    private val whiteboardRepo: WhiteboardRepo,
    private val roomRepo: RoomRepo,
    private val settingsRepo: SettingsRepo,
    private val remoteDbRepo: RemoteDbRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val whiteboardId = savedStateHandle.toRoute<Routes.WhiteboardScreen>().whiteboardId
    private val roomId = savedStateHandle.toRoute<Routes.WhiteboardScreen>().roomId


    private var updatedWhiteboardId = MutableStateFlow(whiteboardId)
    private var isFirstPath = true


    private var _state = MutableStateFlow(WhiteBoardState())
    val state = combine(
        _state,
        settingsRepo.getPreferredCanvasColors(),
        settingsRepo.getPreferredStrokeColors(),
        settingsRepo.getPreferredFillColors()
    ) { state, canvasColors, strokeColors, fillColors ->

        state.copy(
            preferredCanvasColors = canvasColors,
            preferredStrokeColors = strokeColors,
            preferredFillColors = fillColors
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WhiteBoardState()
    )


    init {

        whiteboardId?.let {
            getWhiteboardById(it)
        }
        observePaths()


        if (roomId != null) {
            _state.update { it.copy(mode = Mode.Online(roomId), roomId = roomId) }
            println("Mode Online")
            if (whiteboardId != null) { // Clicked on Already Created Shared Whiteboard, connect to room.
                joinRoom(roomId)
                fetchAllPathsFromServerAndSaveToDb(whiteboardId)
            }
            observeIncomingPaths()
        } else {
            println("Mode Offline")
            _state.update { it.copy(mode = Mode.Offline) }
        }


    }

    private fun joinRoom(roomId: String) {
        println("Test:  " + "Join Room Called")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                roomRepo.joinRoom(roomId, Information.userId) joinCallBack@{ isSuccess ->
                    if (!isSuccess) {
                        _state.update { it.copy(isLoading = false) }
                        return@joinCallBack
                    }
                    viewModelScope.launch(Dispatchers.Main) {
                        println("Test:  " + "Join Room Success")
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                println(e)
            }

        }
    }

    private fun observeIncomingPaths() {
        viewModelScope.launch {
            try {
                roomRepo.observeIncomingPath().collect {
                    println("In View Model path received")
                    if (isFirstPath) {
                        upsertWhiteboard()
                        isFirstPath = false
                    }
                    val updatedPathWithCorrectWhiteboardId =
                        it.copy(whiteboardId = updatedWhiteboardId.value ?: 0, roomId = roomId)
                    insertPath(updatedPathWithCorrectWhiteboardId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    fun onEvent(event: WhiteBoardEvent) {

        when (event) {
            is WhiteBoardEvent.StartDrawing -> {
                println("Is First Path? : $isFirstPath")
                if (isFirstPath) {
                    upsertWhiteboard()
                    println("Whiteboard Id: ${updatedWhiteboardId.value}")
                    isFirstPath = false
                }

                _state.update {
                    it.copy(startingOffset = event.offset)
                }
            }

            is WhiteBoardEvent.Draw -> {
                continuouslyUpdateOffset(event.continuingOffset)
            }

            WhiteBoardEvent.EndDrawing -> {
                state.value.currentPath?.let { drawnPath ->
                    when (drawnPath.drawingTool) {

                        DrawingTool.ERASER -> {
                            when (val mode = state.value.mode) {
                                Mode.Offline -> deletePaths(state.value.pathsToBeDeleted)
                                is Mode.Online -> {
                                    deletePaths(state.value.pathsToBeDeleted)
                                }
                            }

                        }

                        DrawingTool.LASER_PEN -> {
                            _state.update { it.copy(laserPenPath = drawnPath) }
                        }

                        else -> {
                            when (val mode = state.value.mode) {
                                Mode.Offline -> insertPath(drawnPath)
                                is Mode.Online -> {
                                    val pathId = UUID.randomUUID().toString()
                                    val drawnPathWithRoomId =
                                        drawnPath.copy(roomId = roomId, pathId = pathId)
                                    println(drawnPathWithRoomId)

                                    insertPath(drawnPathWithRoomId)
                                    emitPath(mode.roomId, drawnPathWithRoomId)
                                }
                            }
                        }
                    }
                }
                _state.update { it.copy(currentPath = null, pathsToBeDeleted = emptyList()) }
            }

            WhiteBoardEvent.OnFavClick -> {
                _state.update { it.copy(isToolBoxVisible = !it.isToolBoxVisible) }
            }

            WhiteBoardEvent.OnToolBoxClose -> {
                _state.update { it.copy(isToolBoxVisible = false) }
            }

            is WhiteBoardEvent.OnToolSelected -> {
                when (event.drawingTool) {

                    DrawingTool.RECTANGLE, DrawingTool.CIRCLE, DrawingTool.TRIANGLE -> {
                        _state.update { it.copy(selectedDrawingTool = event.drawingTool) }
                    }

                    else -> {
                        _state.update {
                            it.copy(
                                selectedDrawingTool = event.drawingTool,
                                fillColor = Color.Transparent
                            )
                        }

                    }
                }

            }

            is WhiteBoardEvent.CanvasColorChange -> {
                _state.update { it.copy(canvasColor = event.canvasColor) }
                upsertWhiteboard()
            }

            is WhiteBoardEvent.FillColorChange -> {
                _state.update { it.copy(fillColor = event.backgroundColor) }
            }

            is WhiteBoardEvent.StrokeColorChange -> {
                _state.update { it.copy(strokeColor = event.color) }
            }

            is WhiteBoardEvent.OpacitySliderValueChange -> {
                _state.update { it.copy(opacity = event.opacity) }
            }

            is WhiteBoardEvent.StrokeSliderValueChange -> {
                _state.update { it.copy(strokeWidth = event.width) }
            }

            WhiteBoardEvent.OnLaserPathAnimationComplete -> {
                _state.update { it.copy(laserPenPath = null) }
            }

            is WhiteBoardEvent.OnColorPaletteIconClick -> {

                _state.update {
                    it.copy(
                        isColorSelectionDialogOpen = true,
                        selectedColorPaletteType = event.colorPaletteType
                    )
                }
            }

            WhiteBoardEvent.OnColorSelectionDialogDismiss -> {
                _state.update { it.copy(isColorSelectionDialogOpen = false) }
            }

            is WhiteBoardEvent.OnColorSelected -> {

                val newList = updatePreferredColorList(
                    event.color,
                    when (state.value.selectedColorPaletteType) {
                        ColorPaletteType.STROKE -> state.value.preferredStrokeColors
                        ColorPaletteType.FILL -> state.value.preferredFillColors
                        ColorPaletteType.CANVAS -> state.value.preferredCanvasColors
                    }
                )

                when (state.value.selectedColorPaletteType) {
                    ColorPaletteType.STROKE -> {
                        _state.update { it.copy(strokeColor = event.color) }
                    }

                    ColorPaletteType.FILL -> {
                        _state.update { it.copy(fillColor = event.color) }
                    }

                    ColorPaletteType.CANVAS -> {
                        _state.update { it.copy(canvasColor = event.color) }
                        upsertWhiteboard()
                    }
                }

                savePreferredColors(newList, state.value.selectedColorPaletteType)


            }
        }

    }

    private fun emitPath(roomId: String, drawnPath: DrawnPath) {
        viewModelScope.launch {
            try {
                println("Emit path called in Whiteboard ViewModel")
                roomRepo.emitPath(roomId, drawnPath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePaths() {
        viewModelScope.launch {
            println("Updated id: " + updatedWhiteboardId.value)
            updatedWhiteboardId
                .flatMapLatest { id ->
                    pathRepo.getPathsForWhiteboard(id ?: -1)
                }
                .collectLatest { paths ->
                    println("Paths Collected: ${paths}")
                    _state.update { it.copy(pathList = paths) }

                }

        }

    }

    private fun getWhiteboardById(id: Long) {
        viewModelScope.launch {
            val whiteboard = whiteboardRepo.getWhiteboardById(id)
            whiteboard?.let {
                _state.update {
                    it.copy(whiteboardName = whiteboard.name, canvasColor = whiteboard.canvasColor)
                }
            }
        }

    }

    private fun upsertWhiteboard() {
        viewModelScope.launch {

            val date = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val whiteboard = Whiteboard(
                id = updatedWhiteboardId.value,
                name = state.value.whiteboardName,
                canvasColor = state.value.canvasColor,
                lastEdited = date,
                roomId = roomId
            )

            val newId = whiteboardRepo.upsertWhiteboard(whiteboard)
            updatedWhiteboardId.value = newId
        }
    }

    private fun insertPath(path: DrawnPath) {
        viewModelScope.launch {
            pathRepo.insertPath(path)
        }
    }

    private fun deletePaths(paths: List<DrawnPath>) {
        viewModelScope.launch {
            paths.forEach {
                pathRepo.deletePath(it)
            }
        }
    }

    private fun savePreferredColors(
        colors: List<Color>,
        colorPaletteType: ColorPaletteType
    ) {
        viewModelScope.launch {
            settingsRepo.savePreferredColors(colors, colorPaletteType)
        }
    }


    private fun continuouslyUpdateOffset(continuingOffset: Offset) {

        val startOffset = _state.value.startingOffset

        val updatedPath: Path? = when (_state.value.selectedDrawingTool) {

            DrawingTool.PEN, DrawingTool.HIGH_LIGHTER, DrawingTool.LASER_PEN -> {
                createFreeHandPath(start = startOffset, continuingOffset = continuingOffset)
            }

            DrawingTool.ERASER -> {
                updatePathsToBeDeleted(start = startOffset, continuingOffset = continuingOffset)
                createEraserPath(continuingOffset = continuingOffset)
            }

            DrawingTool.LINE -> createLinePath(
                start = startOffset,
                continuingOffset = continuingOffset
            )

            DrawingTool.ARROW -> null

            DrawingTool.RECTANGLE -> createRectanglePath(
                start = startOffset,
                continuingOffset = continuingOffset
            )

            DrawingTool.CIRCLE -> createCircle(
                start = startOffset,
                continuingOffset = continuingOffset
            )

            DrawingTool.TRIANGLE -> createTriangle(
                start = startOffset,
                continuingOffset = continuingOffset
            )


        }

        updatedWhiteboardId.value?.let { id ->
            _state.update {
                it.copy(
                    currentPath = updatedPath?.let { path ->
                        DrawnPath(
                            path = path,
                            drawingTool = _state.value.selectedDrawingTool,
                            strokeWidth = _state.value.strokeWidth,
                            opacity = _state.value.opacity,
                            strokeColor = _state.value.strokeColor,
                            fillColor = _state.value.fillColor,
                            whiteboardId = id,
                            roomId = "roomId",  // these two will be replaced at the time of insert to db
                            pathId = "pathId"   // and emit with actual value
                        )
                    }
                )
            }
        }


    }


    private fun createFreeHandPath(start: Offset, continuingOffset: Offset): Path {
        val existingPath = _state.value.currentPath?.path ?: Path().apply {
            moveTo(start.x, start.y)
        }

        return Path().apply {
            addPath(existingPath)
            lineTo(continuingOffset.x, continuingOffset.y)
        }

    }

    private fun createLinePath(start: Offset, continuingOffset: Offset): Path {
        return Path().apply {
            moveTo(start.x, start.y)
            lineTo(continuingOffset.x, continuingOffset.y)
        }
    }

    private fun createRectanglePath(start: Offset, continuingOffset: Offset): Path {

        val left = minOf(start.x, continuingOffset.x)
        val top = minOf(start.y, continuingOffset.y)
        val right = maxOf(start.x, continuingOffset.x)
        val bottom = maxOf(start.y, continuingOffset.y)

        val width = right - left
        val height = bottom - top

        return Path().apply {
            addRect(
                Rect(
                    offset = Offset(left, top),
                    size = Size(width, height)
                )
            )
        }
    }

    private fun createCircle(start: Offset, continuingOffset: Offset): Path {

        val left = minOf(start.x, continuingOffset.x)
        val top = minOf(start.y, continuingOffset.y)
        val right = maxOf(start.x, continuingOffset.x)
        val bottom = maxOf(start.y, continuingOffset.y)

        val width = right - left
        val height = bottom - top

        return Path().apply {
            addOval(
                Rect(
                    offset = Offset(left, top),
                    size = Size(width, height)
                )
            )
        }
    }

    private fun createTriangle(start: Offset, continuingOffset: Offset): Path {

        val halfWidth = continuingOffset.x - start.x
        val rightMost = Offset(continuingOffset.x, continuingOffset.y)
        val leftMost = Offset(start.x - halfWidth, continuingOffset.y)

        return Path().apply {
            moveTo(start.x, start.y)
            lineTo(rightMost.x, rightMost.y)
            lineTo(leftMost.x, leftMost.y)
            close()
        }
    }


    private fun createEraserPath(continuingOffset: Offset): Path {
        return Path().apply {
            addOval(Rect(center = continuingOffset, radius = 5f))
        }
    }

    private fun updatePathsToBeDeleted(start: Offset, continuingOffset: Offset) {
        val pathsTobeDeleted = _state.value.pathsToBeDeleted.toMutableList()

        state.value.pathList.forEach {
            val bounds = it.path.getBounds()
            if (bounds.contains(start) || bounds.contains(continuingOffset)) {
                if (!pathsTobeDeleted.contains(it)) {
                    pathsTobeDeleted.add(it)
                }
            }
        }

        _state.update { it.copy(pathsToBeDeleted = pathsTobeDeleted) }


    }

    private fun updatePreferredColorList(
        newColor: Color,
        colors: List<Color>
    ): List<Color> {
        return listOf(newColor) + colors.filter { newColor != it }.take(3)
    }

    fun navigateBack(onSuccess: () -> Unit) {
        if (roomId == null) {
            onSuccess()
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            roomRepo.leaveRoom(roomId) {
                viewModelScope.launch(Dispatchers.Main) {
                    println("Navigate back called")
                    _state.update { it.copy(isLoading = false) }
                    onSuccess()
                }
            }
        }
    }

    private fun fetchAllPathsFromServerAndSaveToDb(whiteboardId: Long) {
        viewModelScope.launch {
            try {
                val paths = remoteDbRepo.fetchPathsForWhiteboard(whiteboardId, roomId!!) {
                    if (!it) {
                        showToast("Failed to fetch paths from server")
                        return@fetchPathsForWhiteboard
                    }
                }
                if (paths.isNotEmpty()) {
                    pathRepo.replacePaths(roomId, paths)
                }
            } catch (e: Exception) {
                showToast("Failed to fetch paths from server")
                println(e)
            }
        }

    }


    fun showToast(message: String) {
        _state.update { it.copy(toastMessage = message) }
    }

    fun clearToast() {
        _state.update { it.copy(toastMessage = null) }
    }


}
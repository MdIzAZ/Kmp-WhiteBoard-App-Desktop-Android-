package org.example.whiteboard.presentation.whiteboard


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.presentation.util.ScreenType
import org.example.whiteboard.presentation.util.getUiType
import org.example.whiteboard.presentation.util.rememberScreenSizeInfo
import org.example.whiteboard.presentation.whiteboard.component.ColorSelectionDialog
import org.example.whiteboard.presentation.whiteboard.component.CommandPaletteCard
import org.example.whiteboard.presentation.whiteboard.component.CommandPaletteDrawerContent
import org.example.whiteboard.presentation.whiteboard.component.DrawingToolFab
import org.example.whiteboard.presentation.whiteboard.component.DrawingToolsCardHorizontal
import org.example.whiteboard.presentation.whiteboard.component.DrawingToolsCardVertical
import org.example.whiteboard.presentation.whiteboard.component.TopBarHorizontal
import org.example.whiteboard.presentation.whiteboard.component.TopBarVertical
import javax.swing.Spring.scale
import kotlin.contracts.Returns
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun WhiteBoardScreen(
    modifier: Modifier = Modifier,
    state: WhiteBoardState,
    onEvent: (WhiteBoardEvent) -> Unit,
    onHomeIconClick: () -> Unit,
    onClearToast:()->Unit
) {

    val screenSize = rememberScreenSizeInfo()
    val screenType = screenSize.getUiType()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var isCommandPaletteOpen by remember { mutableStateOf(value = false) }


    var sliderOffsetY by rememberSaveable { mutableFloatStateOf(0f) }
    var sliderOffsetX by rememberSaveable { mutableFloatStateOf(0f) }

    var scale by rememberSaveable { mutableFloatStateOf(1f) }

    val trackHeight = screenSize.height.value * scale
    val trackWidth = screenSize.width.value * scale

    val density = LocalDensity.current
    val verticalRatio = with(density) {
        800.dp.toPx() / trackHeight
    }

    val horizontalRatio = with(density) {
        800.dp.toPx() / trackWidth
    }

    ColorSelectionDialog(
        isOpen = state.isColorSelectionDialogOpen,
        onColorSelected = { color ->
            onEvent(WhiteBoardEvent.OnColorSelected(color))
        },
        onDismissRequest = { onEvent(WhiteBoardEvent.OnColorSelectionDialogDismiss) }
    )

    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            onClearToast()
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { ip ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(ip)
        ) {


            when (screenType) {

                ScreenType.COMPACT -> {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            CommandPaletteDrawerContent(
                                modifier = Modifier,
                                roomId = state.roomId,
                                onCloseClick = {
                                    scope.launch { drawerState.close() }
                                },
                                selectedDrawingTool = state.selectedDrawingTool,
                                canvasColors = state.preferredCanvasColors,
                                selectedCanvasColor = state.canvasColor,
                                onCanvasColorChange = { onEvent(WhiteBoardEvent.CanvasColorChange(it)) },
                                strokeColors = state.preferredStrokeColors,
                                selectedStrokeColor = state.strokeColor,
                                onStrokeColorChange = { onEvent(WhiteBoardEvent.StrokeColorChange(it)) },
                                fillColors = state.preferredFillColors,
                                selectedFillColor = state.fillColor,
                                onFillColorChange = {
                                    onEvent(WhiteBoardEvent.FillColorChange(it))
                                },
                                strokeWidthSliderValue = state.strokeWidth,
                                onStrokeWidthSliderValueChange = {
                                    onEvent(WhiteBoardEvent.StrokeSliderValueChange(it))
                                },
                                opacitySliderValue = state.opacity,
                                onOpacitySliderValueChange = {
                                    onEvent(WhiteBoardEvent.OpacitySliderValueChange(it))
                                },
                                onColorPaletteIconClick = {
                                    onEvent(WhiteBoardEvent.OnColorPaletteIconClick(it))
                                }
                            )
                        },
                    ) {
                        DrawingCanvas(
                            modifier = Modifier,
                            state = state,
                            onEvent = onEvent,
                            scale = scale,
                            offset = Offset(
                                sliderOffsetX * horizontalRatio,
                                sliderOffsetY * verticalRatio
                            ),
                            showSnackBar = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(it)
                                }
                            }
                        )

                        TopBarHorizontal(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(20.dp),
                            onHomeIconClick = onHomeIconClick,
                            onUndoClick = {},
                            onRedoClick = {},
                            onMenuClick = {
                                scope.launch {
                                    if (drawerState.isOpen) drawerState.close()
                                    else drawerState.open()
                                }
                            },
                            onZoomInClick = {
                                scale = (scale + 0.2f).coerceAtMost(5f)
                            },
                            onZoomOutClick = {
                                scale = (scale - 0.2f).coerceAtLeast(0.2f)
                            }
                        )

                        DrawingToolFab(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(20.dp),
                            isVisible = !state.isToolBoxVisible,
                            selectedTool = state.selectedDrawingTool,
                            onClick = { onEvent(WhiteBoardEvent.OnFavClick) },
                        )

                        DrawingToolsCardHorizontal(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(20.dp),
                            selectedTool = state.selectedDrawingTool,
                            onToolClick = { onEvent(WhiteBoardEvent.OnToolSelected(it)) },
                            isToolBoxVisible = state.isToolBoxVisible,
                            onToolBoxCloseClick = { onEvent(WhiteBoardEvent.OnToolBoxClose) },
                        )
                    }


                }

                else -> {
                    DrawingCanvas(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onEvent = onEvent,
                        scale = scale,
                        offset = Offset(sliderOffsetX, sliderOffsetY),
                        showSnackBar = {
                            scope.launch {
                                snackBarHostState.showSnackbar(it)
                            }
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.TopStart)
                            .padding(20.dp)
                    ) {
                        TopBarVertical(
                            onHomeIconClick = onHomeIconClick,
                            onUndoClick = {},
                            onRedoClick = {},
                            onMenuClick = { isCommandPaletteOpen = true },
                            onZoomInClick = {
                                scale = (scale + 0.2f).coerceAtMost(5f)
                            },
                            onZoomOutClick = {
                                scale = (scale - 0.2f).coerceAtLeast(0.2f)
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        CommandPaletteCard(
                            modifier = Modifier,
                            roomId = state.roomId,
                            isVisible = isCommandPaletteOpen,
                            onCloseClick = {
                                isCommandPaletteOpen = false
                            },
                            selectedDrawingTool = state.selectedDrawingTool,
                            canvasColors = state.preferredCanvasColors,
                            selectedCanvasColor = state.canvasColor,
                            onCanvasColorChange = { onEvent(WhiteBoardEvent.CanvasColorChange(it)) },
                            strokeColors = state.preferredStrokeColors,
                            selectedStrokeColor = state.strokeColor,
                            onStrokeColorChange = { onEvent(WhiteBoardEvent.StrokeColorChange(it)) },
                            fillColors = state.preferredFillColors,
                            selectedFillColor = state.fillColor,
                            onFillColorChange = {
                                onEvent(WhiteBoardEvent.FillColorChange(it))
                            },
                            strokeWidthSliderValue = state.strokeWidth,
                            onStrokeWidthSliderValueChange = {
                                onEvent(WhiteBoardEvent.StrokeSliderValueChange(it))
                            },
                            opacitySliderValue = state.opacity,
                            onOpacitySliderValueChange = {
                                onEvent(WhiteBoardEvent.OpacitySliderValueChange(it))
                            },
                            onColorPaletteIconClick = {
                                onEvent(WhiteBoardEvent.OnColorPaletteIconClick(it))
                            }
                        )
                    }

                    DrawingToolFab(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp),
                        isVisible = !state.isToolBoxVisible,
                        selectedTool = state.selectedDrawingTool,
                        onClick = { onEvent(WhiteBoardEvent.OnFavClick) },
                    )

                    DrawingToolsCardVertical(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp),
                        selectedTool = state.selectedDrawingTool,
                        onToolClick = { onEvent(WhiteBoardEvent.OnToolSelected(it)) },
                        isToolBoxVisible = state.isToolBoxVisible,
                        onToolBoxCloseClick = { onEvent(WhiteBoardEvent.OnToolBoxClose) },
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .wrapContentSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(24.dp).size(80.dp).align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(vertical = 64.dp)
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(40.dp)
            ) {

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(0, sliderOffsetY.roundToInt())
                        }
                        .width(32.dp)
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    sliderOffsetY += min(dragAmount.y, trackHeight)
                                }
                            )
                        }
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(Modifier.height(8.dp))

                        for (i in 0..6) {
                            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                            Spacer(Modifier.height(8.dp))
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(sliderOffsetX.roundToInt(), 0f.roundToInt())
                        }
                        .width(60.dp)
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    sliderOffsetX += min(dragAmount.x, trackWidth)
                                }
                            )
                        }
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Spacer(Modifier.width(8.dp))

                        for (i in 0..4) {
                            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 2.dp)
                            Spacer(Modifier.width(8.dp))
                        }

                        Spacer(Modifier.width(8.dp))
                    }
                }
            }


        }
    }


}


@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    state: WhiteBoardState,
    onEvent: (WhiteBoardEvent) -> Unit,
    showSnackBar: (String) -> Unit,
    scale: Float,
    offset: Offset
) {

    val textMeasurer = rememberTextMeasurer()


    Canvas(
        modifier = modifier
            .size(800.dp)
            .background(state.canvasColor)
            .pointerInput(offset, scale) {
                detectDragGestures(
                    onDragStart = { pos ->
                        if (scale != 1f) {
                            showSnackBar("Please reset zoom level to 100% before drawing")
                            return@detectDragGestures
                        } else {
                            val world = (pos + offset)
                            onEvent(WhiteBoardEvent.StartDrawing(world))
                        }
                    },
                    onDrag = { change, _ ->
                        val world = (change.position + offset)
                        if (scale == 1f) {
                            onEvent(WhiteBoardEvent.Draw(world))
                        }
                    },
                    onDragEnd = {
                        if (scale == 1f) {
                            onEvent(WhiteBoardEvent.EndDrawing)
                        }
                    }
                )
            }
    ) {
        withTransform({
            scale(scale)
            translate(-offset.x, -offset.y)
        }) {

            drawText(
                text = "Hello Canvas",
                textMeasurer = textMeasurer
            )

            state.pathList.forEach { drawCustomPath(it) }
            state.currentPath?.let { drawCustomPath(it) }
        }
    }

    // Laser path animation same as before
    AnimateLaserPath(
        laserPath = state.laserPenPath,
        onAnimationComplete = { onEvent(WhiteBoardEvent.OnLaserPathAnimationComplete) }
    )
}


private fun DrawScope.drawCustomPath(path: DrawnPath) {
    when (path.fillColor) {
        Color.Transparent -> {
            drawPath(
                path = path.path,
                color = path.strokeColor.copy(alpha = path.opacity / 100),
                style = Stroke(width = path.strokeWidth.dp.toPx()),
            )
        }

        else -> {
            path.fillColor?.let {
                drawPath(
                    path = path.path,
                    color = path.fillColor.copy(alpha = path.opacity / 100),
                    style = Fill,
                )
            }
        }
    }
}


@Composable
fun AnimateLaserPath(
    laserPath: DrawnPath? = null,
    onAnimationComplete: () -> Unit,
) {

    val animationProgress = remember { Animatable(initialValue = 1f) }

    LaunchedEffect(laserPath) {
        laserPath?.let {
            animationProgress.animateTo(targetValue = 0f, animationSpec = tween(1000))
            onAnimationComplete()
            animationProgress.snapTo(targetValue = 1f)
        }
    }

    val trimmedPath = Path()

    PathMeasure().apply {
        setPath(path = laserPath?.path, forceClosed = false)
        getSegment(
            startDistance = length * (1 - animationProgress.value),
            stopDistance = length,
            destination = trimmedPath
            /*
                stores resulting path segment in trimmedPath
                Although no doing trimmedPath = SomePath() || Still it is assigning value to trimmedPath
            */
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        laserPath?.let {
            drawPath(
                path = trimmedPath,
                laserPath.strokeColor,
                style = Stroke(width = laserPath.strokeWidth.dp.toPx())
            )
        }
    }
}


private fun screenToCanvas(screenPos: Offset, offset: Offset, scale: Float): Offset {
    return (screenPos - offset) / scale
}



































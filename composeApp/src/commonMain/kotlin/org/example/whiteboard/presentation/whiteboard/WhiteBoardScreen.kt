package org.example.whiteboard.presentation.whiteboard


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
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

@Composable
fun WhiteBoardScreen(
    modifier: Modifier = Modifier,
    state: WhiteBoardState,
    onEvent: (WhiteBoardEvent) -> Unit,
    onHomeIconClick: () -> Unit
) {

    val screenSize = rememberScreenSizeInfo()
    val screenType = screenSize.getUiType()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var isCommandPaletteOpen by remember { mutableStateOf(value = false) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val zoomState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f, 15f)
        offset += panChange
    }

    ColorSelectionDialog(
        isOpen = state.isColorSelectionDialogOpen,
        onColorSelected = { color ->
            onEvent(WhiteBoardEvent.OnColorSelected(color))
        },
        onDismissRequest = { onEvent(WhiteBoardEvent.OnColorSelectionDialogDismiss) }
    )


    Scaffold {ip->
        Box(modifier = modifier.fillMaxSize().padding(ip)) {

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
                            modifier = Modifier.fillMaxSize().transformable(zoomState),
                            state = state,
                            onEvent = onEvent,
                            scale = scale,
                            offset = offset
                        )

                        TopBarHorizontal(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(20.dp),
                            onHomeIconClick = onHomeIconClick,
                            onUndoClick = {

                            },
                            onRedoClick = {

                            },
                            onMenuClick = {
                                scope.launch {
                                    if (drawerState.isOpen) drawerState.close()
                                    else drawerState.open()
                                }
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
                        modifier = Modifier.fillMaxSize().transformable(zoomState),
                        state = state,
                        onEvent = onEvent,
                        scale = scale,
                        offset = offset
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
                            onMenuClick = { isCommandPaletteOpen = true }
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


        }
    }




}


@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    state: WhiteBoardState,
    onEvent: (WhiteBoardEvent) -> Unit,
    scale: Float,
    offset: Offset
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val inverseScale = 1f / scale.coerceAtLeast(0.01f) // Avoid division by zero (still needed for potential future use)
    val canvasSizeModifier = Modifier
        .fillMaxSize()
        .background(state.canvasColor)
        .onSizeChanged { size ->
            canvasSize = size
        }
    // Removed the .then(if (scale < 1f) Modifier.size(...) ) to fix pivot and input issues

    Canvas(
        modifier = modifier
            .then(canvasSizeModifier)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        val adjusted = (it - offset) / scale
                        onEvent(WhiteBoardEvent.StartDrawing(adjusted))
                    },
                    onDrag = { change, _ ->
                        val adjusted = (change.position - offset) / scale
                        onEvent(WhiteBoardEvent.Draw(adjusted))
                    },
                    onDragEnd = {
                        onEvent(WhiteBoardEvent.EndDrawing)
                    }
                )
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        withTransform({
            translate(offset.x, offset.y)
            scale(scale, scale, pivot = Offset(canvasWidth / 2f, canvasHeight / 2f))
        }) {
            state.pathList.forEach {
                drawCustomPath(it)
            }
            state.currentPath?.let {
                drawCustomPath(it)
            }
        }
    }

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
            drawPath(
                path = path.path,
                color = path.fillColor.copy(alpha = path.opacity / 100),
                style = Fill,
            )
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




































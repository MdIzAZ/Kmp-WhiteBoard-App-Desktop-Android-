package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.DrawingTool


@Composable
fun CommandPaletteDrawerContent(
    modifier: Modifier,
    roomId: String,
    selectedDrawingTool: DrawingTool,
    onCloseClick: () -> Unit,
    canvasColors: List<Color>,
    selectedCanvasColor: Color,
    onCanvasColorChange: (Color) -> Unit,
    strokeColors: List<Color>,
    selectedStrokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    fillColors: List<Color>,
    selectedFillColor: Color,
    onFillColorChange: (Color) -> Unit,
    strokeWidthSliderValue: Float,
    onStrokeWidthSliderValueChange: (Float) -> Unit,
    opacitySliderValue: Float,
    onOpacitySliderValueChange: (Float) -> Unit,
    onColorPaletteIconClick: (ColorPaletteType) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
    ) {
        CommandPaletteContent(
            modifier = Modifier,
            roomId = roomId,
            selectedDrawingTool = selectedDrawingTool,
            canvasColors = canvasColors,
            selectedCanvasColor = selectedCanvasColor,
            onCanvasColorChange = onCanvasColorChange,
            strokeColors = strokeColors,
            selectedStrokeColor = selectedStrokeColor,
            onStrokeColorChange = onStrokeColorChange,
            fillColors = fillColors,
            selectedFillColor = selectedFillColor,
            onFillColorChange = onFillColorChange,
            onCloseClick = onCloseClick,
            strokeWidthSliderValue = strokeWidthSliderValue,
            onStrokeWidthSliderValueChange = onStrokeWidthSliderValueChange,
            opacitySliderValue = opacitySliderValue,
            onOpacitySliderValueChange = onOpacitySliderValueChange,
            onColorPaletteIconClick = onColorPaletteIconClick
        )
    }
}

@Composable
fun CommandPaletteCard(
    modifier: Modifier,
    roomId: String,
    isVisible: Boolean,
    selectedDrawingTool: DrawingTool,
    onCloseClick: () -> Unit,
    canvasColors: List<Color>,
    selectedCanvasColor: Color,
    onCanvasColorChange: (Color) -> Unit,
    strokeColors: List<Color>,
    selectedStrokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    fillColors: List<Color>,
    selectedFillColor: Color,
    onFillColorChange: (Color) -> Unit,
    strokeWidthSliderValue: Float,
    onStrokeWidthSliderValueChange: (Float) -> Unit,
    opacitySliderValue: Float,
    onOpacitySliderValueChange: (Float) -> Unit,
    onColorPaletteIconClick: (ColorPaletteType) -> Unit

) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ElevatedCard(
            modifier = modifier
                .width(250.dp)
        ) {
            CommandPaletteContent(
                modifier = Modifier,
                roomId = roomId,
                selectedDrawingTool = selectedDrawingTool,
                canvasColors = canvasColors,
                selectedCanvasColor = selectedCanvasColor,
                onCanvasColorChange = onCanvasColorChange,
                strokeColors = strokeColors,
                selectedStrokeColor = selectedStrokeColor,
                onStrokeColorChange = onStrokeColorChange,
                fillColors = fillColors,
                selectedFillColor = selectedFillColor,
                onFillColorChange = onFillColorChange,
                onCloseClick = onCloseClick,
                strokeWidthSliderValue = strokeWidthSliderValue,
                onStrokeWidthSliderValueChange = onStrokeWidthSliderValueChange,
                opacitySliderValue = opacitySliderValue,
                onOpacitySliderValueChange = onOpacitySliderValueChange,
                onColorPaletteIconClick = onColorPaletteIconClick
            )
        }
    }
}


@Composable
fun CommandPaletteContent(
    modifier: Modifier,
    selectedDrawingTool: DrawingTool,
    roomId: String,
    onCloseClick: () -> Unit,
    canvasColors: List<Color>,
    selectedCanvasColor: Color,
    onCanvasColorChange: (Color) -> Unit,
    strokeColors: List<Color>,
    selectedStrokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    fillColors: List<Color>,
    selectedFillColor: Color,
    onFillColorChange: (Color) -> Unit,
    strokeWidthSliderValue: Float,
    onStrokeWidthSliderValueChange: (Float) -> Unit,
    opacitySliderValue: Float,
    onOpacitySliderValueChange: (Float) -> Unit,
    onColorPaletteIconClick: (ColorPaletteType) -> Unit
) {

    val updatedCanvasColors = listOf(Color.White) + canvasColors
    val updatedStrokeColors = listOf(Color.Black) + strokeColors

    Column(
        modifier = modifier.padding(10.dp).verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Untitled",
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = onCloseClick,
                content = {
                    Icon(Icons.Default.Close, "Close")
                }
            )
        }

        HorizontalDivider(modifier = Modifier.height(20.dp))

        ColorSection(
            selectionTitle = "Canvas",
            colors = updatedCanvasColors,
            selectedColor = selectedCanvasColor,
            onColorClick = onCanvasColorChange,
            onColorWheelClick = {onColorPaletteIconClick(ColorPaletteType.CANVAS)}
        )
        ColorSection(
            selectionTitle = "Stroke",
            colors = updatedStrokeColors,
            selectedColor = selectedStrokeColor,
            onColorClick = onStrokeColorChange,
            onColorWheelClick = { onColorPaletteIconClick(ColorPaletteType.STROKE) }
        )

        when (selectedDrawingTool) {


            DrawingTool.CIRCLE, DrawingTool.RECTANGLE, DrawingTool.TRIANGLE -> {
                Spacer(Modifier.height(20.dp))
                ColorSection(
                    selectionTitle = "Fill",
                    colors = fillColors,
                    isFillColors = true,
                    selectedColor = selectedFillColor,
                    onColorClick = onFillColorChange,
                    onColorWheelClick = { onColorPaletteIconClick(ColorPaletteType.FILL) }
                )
            }

            else -> {}
        }

        Spacer(Modifier.height(20.dp))
        SliderSection(
            title = "Stroke Width",
            sliderValueRange = 1f..25f,
            sliderValue = strokeWidthSliderValue,
            onSliderWidthValueChange = onStrokeWidthSliderValueChange
        )

        Spacer(Modifier.height(10.dp))
        SliderSection(
            title = "Opacity",
            sliderValueRange = 1f..100f,
            sliderValue = opacitySliderValue,
            onSliderWidthValueChange = onOpacitySliderValueChange
        )

        if (roomId.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            RoomIdWithCopyBtn(
                roomId = roomId
            )
        }

    }
}





















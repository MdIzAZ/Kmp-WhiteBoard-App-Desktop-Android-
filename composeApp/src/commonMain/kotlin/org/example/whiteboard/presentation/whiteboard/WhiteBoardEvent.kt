package org.example.whiteboard.presentation.whiteboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.DrawingTool

sealed class WhiteBoardEvent {
    data object EndDrawing : WhiteBoardEvent()
    data object OnToolBoxClose : WhiteBoardEvent()
    data object OnFavClick : WhiteBoardEvent()
    data object OnLaserPathAnimationComplete : WhiteBoardEvent()
    data object OnColorSelectionDialogDismiss : WhiteBoardEvent()
    data class StartDrawing(val offset: Offset) : WhiteBoardEvent()
    data class Draw(val continuingOffset: Offset) : WhiteBoardEvent()
    data class OnToolSelected(val drawingTool: DrawingTool) : WhiteBoardEvent()
    data class StrokeSliderValueChange(val width: Float) : WhiteBoardEvent()
    data class OpacitySliderValueChange(val opacity: Float) : WhiteBoardEvent()
    data class StrokeColorChange(val color: Color) : WhiteBoardEvent()
    data class CanvasColorChange(val canvasColor: Color) : WhiteBoardEvent()
    data class FillColorChange(val backgroundColor: Color) : WhiteBoardEvent()
    data class OnColorPaletteIconClick(val colorPaletteType: ColorPaletteType) : WhiteBoardEvent()
    data class OnColorSelected(val color: Color) : WhiteBoardEvent()

}
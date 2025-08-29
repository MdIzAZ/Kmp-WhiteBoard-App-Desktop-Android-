package org.example.whiteboard.presentation.whiteboard


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.DrawingTool
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.domain.model.Mode
import org.example.whiteboard.presentation.theme.defaultCanvasColors
import org.example.whiteboard.presentation.theme.defaultDrawingColors

data class WhiteBoardState(
    var mode: Mode = Mode.Offline,
    var isLoading: Boolean = false,
    var selectedDrawingTool: DrawingTool = DrawingTool.PEN,
    var isToolBoxVisible: Boolean = true,
    var isColorSelectionDialogOpen: Boolean = false,
    var currentPath: DrawnPath? = null,
    var laserPenPath: DrawnPath? = null,
    var pathList: List<DrawnPath> = emptyList(),
    var pathsToBeDeleted: List<DrawnPath> = emptyList(),
    var startingOffset: Offset = Offset.Zero,
    val strokeWidth: Float = 5f,
    val opacity: Float = 100f,
    val strokeColor: Color = Color.Black,
    val fillColor: Color = Color.Transparent,
    val canvasColor: Color = Color.White,
    val whiteboardName: String = "Untitled",
    val roomId: String = "",
    val selectedColorPaletteType: ColorPaletteType = ColorPaletteType.STROKE,
    val preferredCanvasColors: List<Color> = defaultCanvasColors,
    val preferredStrokeColors: List<Color> = defaultDrawingColors,
    val preferredFillColors: List<Color> = defaultDrawingColors,

    val toastMessage: String? = null,

)
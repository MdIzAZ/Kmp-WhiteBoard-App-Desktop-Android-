package org.example.whiteboard.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class DrawnPath(
    val id: Long? = null,  //local
    val pathId:String?,
    val path: Path,
    val drawingTool: DrawingTool,
    val strokeWidth: Float,
    val opacity: Float,
    val strokeColor: Color,
    val fillColor: Color,
    val whiteboardId: Long,
    val roomId: String?
)

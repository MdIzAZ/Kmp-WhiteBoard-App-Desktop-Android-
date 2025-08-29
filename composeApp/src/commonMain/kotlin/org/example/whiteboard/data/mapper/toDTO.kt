package org.example.whiteboard.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.PathParser
import org.example.whiteboard.data.remote.dto.DrawnPathDTO
import org.example.whiteboard.domain.model.DrawingTool
import org.example.whiteboard.domain.model.DrawnPath


// Remote
fun DrawnPath.toDTO(): DrawnPathDTO {
    return DrawnPathDTO(
        pathId = pathId!!,  // null while drawing , must not null while erasing
        roomId = roomId!!,  // must not be null . else crash app to make sure passing and roomId each time
        path = serializePath(path),
        drawingTool = drawingTool.name,
        strokeWidth = strokeWidth,
        opacity = opacity,
        strokeColor = strokeColor.toArgb().toLong(),
        fillColor = fillColor.toArgb().toLong(),
        whiteboardId = whiteboardId
    )
}


fun DrawnPathDTO.toDrawnPath(): DrawnPath {
    return DrawnPath(
        pathId = pathId,
        roomId = roomId,
        path = deserializePath(path),
        drawingTool = DrawingTool.valueOf(drawingTool),
        strokeWidth = strokeWidth,
        opacity = opacity,
        strokeColor = Color(strokeColor),
        fillColor = fillColor?.let { Color(it) } ?: Color.Transparent,
        whiteboardId = whiteboardId
    )
}


private fun serializePath(path: Path): String {
    val pathString = StringBuilder()
    val pathMeasure = PathMeasure()
    pathMeasure.setPath(path, false)
    val length = pathMeasure.length
    var distance = 0f
    while (distance < length) {
        val pos = pathMeasure.getPosition(distance)
        if (distance == 0f) {
            pathString.append("M${pos.x},${pos.y}")
        } else {
            pathString.append("L${pos.x},${pos.y}")
        }
        distance += 1f
    }
//        pathString.append("Z")
    return pathString.toString()
}

private fun deserializePath(pathString: String): Path {
    val pathParser = PathParser().parsePathString(pathString)
    return pathParser.toPath()
}

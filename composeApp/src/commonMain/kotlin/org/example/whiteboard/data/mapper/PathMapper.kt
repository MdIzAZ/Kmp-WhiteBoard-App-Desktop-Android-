package org.example.whiteboard.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.PathParser
import okio.Path.Companion.toPath
import org.example.whiteboard.data.database.entity.PathEntity
import org.example.whiteboard.data.mapper.deserializePath
import org.example.whiteboard.data.remote.dto.PathResponse
import org.example.whiteboard.domain.model.DrawingTool
import org.example.whiteboard.domain.model.DrawnPath

fun DrawnPath.toPathEntity() = PathEntity(
    id = id,
    pathId = pathId,
    drawingTool = drawingTool,
    path = path,
    strokeWidth = strokeWidth,
    opacity = opacity,
    strokeColor = strokeColor.toArgb(),
    fillColor = fillColor.toArgb(),
    whiteboardId = whiteboardId,
    roomId = roomId
)


fun PathEntity.toDrawnPath() = DrawnPath(
    id = id,
    pathId = pathId,
    drawingTool = drawingTool,
    path = path,
    strokeWidth = strokeWidth,
    opacity = opacity,
    strokeColor = Color(strokeColor),
    fillColor = fillColor?.let { Color(it) } ?: Color.Transparent,
    whiteboardId = whiteboardId,
    roomId = roomId
)


fun PathResponse.toDrawnPath(whiteboardIdLocal: Long) = DrawnPath(
    pathId = pathId,
    roomId = roomId,
    drawingTool = DrawingTool.valueOf(drawingTool),
    path = deserializePath(pathString),
    strokeWidth = strokeWidth.toFloat(),
    opacity = opacity.toFloat(),
    strokeColor = Color(strokeColor),
    fillColor = fillColor?.let { Color(it) } ?: Color.Transparent,
    whiteboardId = whiteboardIdLocal
)


private fun deserializePath(pathString: String): Path {
    val pathParser = PathParser().parsePathString(pathString)
    return pathParser.toPath()
}

fun List<PathEntity>.toDrawnPathList() = map { it.toDrawnPath() }
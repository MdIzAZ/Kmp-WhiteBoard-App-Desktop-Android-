package org.example.whiteboard.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.example.whiteboard.data.database.entity.PathEntity
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
    fillColor = Color(fillColor),
    whiteboardId = whiteboardId,
    roomId = roomId
)


fun List<PathEntity>.toDrawnPathList() = map { it.toDrawnPath() }
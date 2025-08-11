package org.example.whiteboard.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.example.whiteboard.data.database.entity.WhiteboardEntity
import org.example.whiteboard.domain.model.Whiteboard

fun Whiteboard.toWhiteboardEntity() = WhiteboardEntity(
    id = id,
    name = name,
    lastEdited = lastEdited,
    roomId = roomId,
    canvasColor = canvasColor.toArgb()
)

fun WhiteboardEntity.toWhiteboard() = Whiteboard(
    id = id,
    name = name,
    lastEdited = lastEdited,
    roomId = roomId,
    canvasColor = Color(canvasColor)
)

fun List<WhiteboardEntity>.toWhiteboardList() = map { it.toWhiteboard() }
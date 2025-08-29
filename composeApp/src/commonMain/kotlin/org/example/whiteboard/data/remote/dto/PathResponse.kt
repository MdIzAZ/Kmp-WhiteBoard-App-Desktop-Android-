package org.example.whiteboard.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PathResponse(
    val createdAt: String,
    val drawingTool: String,
    val fillColor: Int?,
    val opacity: Double,
    val pathId: String,
    val pathString: String,
    val strokeColor: Int,
    val strokeWidth: Int,
    @SerialName("whiteboardId")
    val roomId: String
)

@Serializable
data class PathsResponse(
    val paths: List<PathResponse>
)
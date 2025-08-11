package org.example.whiteboard.data.remote.dto
import kotlinx.serialization.Serializable

@Serializable
data class DrawnPathDTO(
    val id:Long? = null,  //local
    val pathId: String,
    val roomId: String,
    val path: String,
    val drawingTool: String,
    val strokeWidth: Float,
    val opacity: Float,
    val strokeColor: Long,
    val fillColor: Long,
    val whiteboardId: Long
)
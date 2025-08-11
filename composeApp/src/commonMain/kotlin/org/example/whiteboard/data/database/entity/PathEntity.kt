package org.example.whiteboard.data.database.entity

import androidx.compose.ui.graphics.Path
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.whiteboard.data.util.Constants.PATH_TABLE_NAME
import org.example.whiteboard.domain.model.DrawingTool


@Entity(tableName = PATH_TABLE_NAME)
data class PathEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null, // unique in local DB
    val pathId: String?, // unique globally
    val drawingTool: DrawingTool,
    val path: Path,
    val strokeWidth: Float,
    val opacity: Float,
    val strokeColor: Int,
    val fillColor: Int,
    val whiteboardId: Long,  // unique in local DB
    val roomId: String?  // unique globally
)

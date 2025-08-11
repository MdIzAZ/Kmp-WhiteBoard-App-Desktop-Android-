package org.example.whiteboard.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.whiteboard.data.util.Constants.WHITEBOARD_TABLE_NAME

@Entity(
    tableName = WHITEBOARD_TABLE_NAME
)
data class WhiteboardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,  // unique in local DB
    val roomId: String?, // unique globally
    val name: String,
    val lastEdited: LocalDate,
    val canvasColor: Int
)
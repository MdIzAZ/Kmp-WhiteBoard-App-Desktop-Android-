package org.example.whiteboard.domain.repo

import org.example.whiteboard.data.database.entity.PathEntity
import org.example.whiteboard.domain.model.DrawnPath

interface RemoteDbRepo {

    suspend fun deleteRoom(roomId: String, onResult: suspend (Boolean) -> Unit)

    suspend fun deleteAllPathsForRoom(roomId: String, onResult: (Boolean) -> Unit)

    suspend fun fetchPathsForWhiteboard(
        whiteboardId: Long,
        roomId: String,
        onResult: (Boolean) -> Unit
    ): List<DrawnPath>

}
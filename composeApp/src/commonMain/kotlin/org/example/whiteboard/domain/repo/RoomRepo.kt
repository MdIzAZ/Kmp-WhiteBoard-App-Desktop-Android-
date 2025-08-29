package org.example.whiteboard.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.domain.model.DrawnPath

interface RoomRepo {

    suspend fun joinRoom(roomId: String, userId: String, onResult: (Boolean) -> Unit)

    suspend fun emitPath(roomId: String, drawnPath: DrawnPath)

    fun observeIncomingPath(): Flow<DrawnPath>

    suspend fun leaveRoom(roomId: String, onResult: (Boolean) -> Unit)



}

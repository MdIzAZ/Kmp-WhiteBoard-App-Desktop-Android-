package org.example.whiteboard.data.repo

import org.example.whiteboard.data.mapper.toDrawnPath
import org.example.whiteboard.data.remote.apis.RemoteDbApi
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.domain.repo.RemoteDbRepo

class RemoteDbRepoImp(
    private val remoteDbApi: RemoteDbApi
): RemoteDbRepo {

    override suspend fun deleteRoom(
        roomId: String,
        onResult: suspend (Boolean) -> Unit
    ) {
        remoteDbApi.deleteRoom(roomId, onResult)
    }

    override suspend fun deleteAllPathsForRoom(
        roomId: String,
        onResult: (Boolean) -> Unit
    ) {

    }

    override suspend fun fetchPathsForWhiteboard(
        whiteboardId: Long,
        roomId: String,
        onResult: (Boolean) -> Unit
    ): List<DrawnPath> {
        return remoteDbApi.fetchPathsForWhiteboard(roomId, onResult).paths.map {
            it.toDrawnPath(whiteboardId)
        }
    }


}
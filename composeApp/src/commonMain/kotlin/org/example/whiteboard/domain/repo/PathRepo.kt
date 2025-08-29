package org.example.whiteboard.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.domain.model.DrawnPath

interface PathRepo {

    suspend fun insertPath(path: DrawnPath)

    suspend fun replacePaths(roomId:String, paths: List<DrawnPath>)

    suspend fun deletePath(path: DrawnPath)

    fun getPathsForWhiteboard(whiteboardId: Long): Flow<List<DrawnPath>>

    suspend fun deletePathsOfWhiteboard(whiteboardId: Long)

    suspend fun deleteAllPaths()



}
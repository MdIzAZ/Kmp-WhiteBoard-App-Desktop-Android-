package org.example.whiteboard.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.whiteboard.data.database.dao.PathDao
import org.example.whiteboard.data.mapper.toDrawnPathList
import org.example.whiteboard.data.mapper.toPathEntity
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.domain.repo.PathRepo

class PathRepoImp(
    private val pathDao: PathDao,
): PathRepo {


    override suspend fun insertPath(path: DrawnPath) {
        pathDao.insertPath(path.toPathEntity())
    }

    override suspend fun replacePaths(roomId: String, paths: List<DrawnPath>) {
        pathDao.replacePaths(roomId, paths.map { it.toPathEntity() })
    }

    override suspend fun deletePath(path: DrawnPath) {
        pathDao.deletePath(path.toPathEntity())
    }

    override fun getPathsForWhiteboard(whiteboardId: Long): Flow<List<DrawnPath>> {
        return pathDao.getPathsForWhiteboard(whiteboardId).map {
            it.toDrawnPathList()
        }
    }

    override suspend fun deletePathsOfWhiteboard(whiteboardId: Long) {
        pathDao.deletePathsOfWhiteboard(whiteboardId)
    }

    override suspend fun deleteAllPaths() {
         pathDao.deleteAllPaths()
    }


}
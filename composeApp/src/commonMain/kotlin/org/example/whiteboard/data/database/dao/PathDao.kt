package org.example.whiteboard.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.data.database.entity.PathEntity
import org.example.whiteboard.data.util.Constants.PATH_TABLE_NAME

@Dao
interface PathDao {

    @Upsert
    suspend fun insertPath(path: PathEntity)

    @Upsert
    suspend fun insetPaths(paths: List<PathEntity>)

    @Query("DELETE FROM $PATH_TABLE_NAME WHERE roomId = :roomId")
    suspend fun deletePathsOfARoom(roomId: String)

    @Transaction
    suspend fun replacePaths(roomId: String, paths: List<PathEntity>) {
        deletePathsOfARoom(roomId)
        insetPaths(paths)
    }

    @Delete
    suspend fun deletePath(path: PathEntity)

    @Query("SELECT * FROM $PATH_TABLE_NAME WHERE whiteboardId = :whiteboardId")
    fun getPathsForWhiteboard(whiteboardId: Long): Flow<List<PathEntity>>


    @Query("DELETE FROM $PATH_TABLE_NAME WHERE whiteboardId = :whiteboardId")
    suspend fun deletePathsOfWhiteboard(whiteboardId: Long)

    @Query("DELETE FROM $PATH_TABLE_NAME")
    suspend fun deleteAllPaths()
}
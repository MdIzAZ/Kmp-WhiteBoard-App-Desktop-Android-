package org.example.whiteboard.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.data.database.entity.PathEntity
import org.example.whiteboard.data.util.Constants.PATH_TABLE_NAME

@Dao
interface PathDao {

    @Upsert
    suspend fun insertPath(path: PathEntity)

    @Delete
    suspend fun deletePath(path: PathEntity)

    @Query("SELECT * FROM $PATH_TABLE_NAME WHERE whiteboardId = :whiteboardId")
    fun getPathsForWhiteboard(whiteboardId: Long): Flow<List<PathEntity>>
}
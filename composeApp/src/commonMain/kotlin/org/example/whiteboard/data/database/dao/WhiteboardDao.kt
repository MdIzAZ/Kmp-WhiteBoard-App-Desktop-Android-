package org.example.whiteboard.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.data.database.entity.WhiteboardEntity
import org.example.whiteboard.data.util.Constants.WHITEBOARD_TABLE_NAME
import org.example.whiteboard.domain.model.Whiteboard

@Dao
interface WhiteboardDao {

    @Insert
    suspend fun insertWhiteboard(whiteboard: WhiteboardEntity): Long

    @Upsert
    suspend fun insertWhiteboards(whiteboards: List<WhiteboardEntity>) : List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWhiteboard(whiteboard: WhiteboardEntity)

    @Query("SELECT * FROM $WHITEBOARD_TABLE_NAME ORDER BY lastEdited DESC")
    fun getAllWhiteboards(): Flow<List<WhiteboardEntity>>


    @Query("UPDATE $WHITEBOARD_TABLE_NAME SET name = :newName WHERE id = :id")
    suspend fun renameWhiteboard(id: Long, newName: String)

    @Query("SELECT * FROM $WHITEBOARD_TABLE_NAME WHERE id = :id")
    suspend fun getWhiteboardById(id: Long): WhiteboardEntity?

    @Query("DELETE FROM $WHITEBOARD_TABLE_NAME WHERE id = :id")
    suspend fun deleteWhiteboard(id: Long)

    @Query("DELETE FROM $WHITEBOARD_TABLE_NAME")
    suspend fun deleteAllWhiteboards()
}
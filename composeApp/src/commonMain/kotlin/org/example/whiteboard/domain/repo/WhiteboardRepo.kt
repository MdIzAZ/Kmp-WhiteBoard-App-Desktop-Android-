package org.example.whiteboard.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.domain.model.Whiteboard

interface WhiteboardRepo {

    suspend fun upsertWhiteboard(whiteboard: Whiteboard): Long

    suspend fun insertWhiteboards(whiteboards: List<Whiteboard>): List<Long>

    suspend fun getWhiteboardById(id: Long): Whiteboard?

    suspend fun renameWhiteboard(id: Long, newName: String)

    suspend fun deleteWhiteboard(id: Long)

    fun getAllWhiteboards(): Flow<List<Whiteboard>>

    suspend fun deleteAllWhiteboards()

}
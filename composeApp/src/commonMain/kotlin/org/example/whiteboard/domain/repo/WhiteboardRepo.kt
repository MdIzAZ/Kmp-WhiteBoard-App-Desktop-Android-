package org.example.whiteboard.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.domain.model.Whiteboard

interface WhiteboardRepo {

    suspend fun upsertWhiteboard(whiteboard: Whiteboard): Long

    suspend fun getWhiteboardById(id: Long): Whiteboard?

    fun getAllWhiteboards(): Flow<List<Whiteboard>>

}
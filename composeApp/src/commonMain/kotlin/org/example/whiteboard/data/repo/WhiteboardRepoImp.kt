package org.example.whiteboard.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.whiteboard.data.database.dao.WhiteboardDao
import org.example.whiteboard.data.mapper.toWhiteboard
import org.example.whiteboard.data.mapper.toWhiteboardEntity
import org.example.whiteboard.data.mapper.toWhiteboardList
import org.example.whiteboard.domain.model.Whiteboard
import org.example.whiteboard.domain.repo.WhiteboardRepo

class WhiteboardRepoImp(
    private val dao: WhiteboardDao
) : WhiteboardRepo {

    override suspend fun upsertWhiteboard(whiteboard: Whiteboard): Long {
        return if (whiteboard.id == null) {
            dao.insertWhiteboard(whiteboard.toWhiteboardEntity())
        } else {
            dao.updateWhiteboard(whiteboard.toWhiteboardEntity())
            whiteboard.id
        }
    }

    override suspend fun getWhiteboardById(id: Long): Whiteboard? {
        return dao.getWhiteboardById(id)?.toWhiteboard()
    }

    override fun getAllWhiteboards(): Flow<List<Whiteboard>> {
        return dao.getAllWhiteboards().map {
            it.toWhiteboardList()
        }
    }
}
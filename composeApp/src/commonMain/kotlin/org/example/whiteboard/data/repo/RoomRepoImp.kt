package org.example.whiteboard.data.repo

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.example.whiteboard.data.mapper.toDTO
import org.example.whiteboard.data.mapper.toDrawnPath
import org.example.whiteboard.data.remote.SocketManager
import org.example.whiteboard.data.remote.dto.DrawnPathDTO
import org.example.whiteboard.domain.model.DrawnPath
import org.example.whiteboard.domain.repo.RoomRepo

class RoomRepoImp() : RoomRepo {

    private val socketManager = SocketManager()

    override suspend fun joinRoom(roomId: String, userId: String, onResult: (Boolean) -> Unit) {

        if (!socketManager.isConnected()) {
            socketManager.connect() {
                socketManager.joinRoom(roomId, userId) { isSuccess, msg ->
                    println("Is Success : $isSuccess")
                    onResult(isSuccess)
                }
            }
        } else {
            socketManager.joinRoom(roomId, userId) { isSuccess, msg ->
                onResult(isSuccess)
            }
        }


    }

    override suspend fun emitPath(roomId: String, drawnPath: DrawnPath) {
        if (!socketManager.isConnected()) return

        println("Emit path called in Room Repo Imp")
        val drawnPathDTO = drawnPath.toDTO()
        val roomJson = JsonPrimitive(roomId)

        val pathJson = Json.encodeToJsonElement(drawnPathDTO)

        val jsonObj = buildJsonObject {
            put("roomId", roomJson)
            put("pathData", pathJson)
        }

        println("Json Obj: " + jsonObj)

        socketManager.emitPath(jsonObj)
    }

    override suspend fun erasePaths(
        paths: List<String>,
        roomId: String,
        onResult: (Boolean, String) -> Unit
    ) {

        socketManager.erase(paths, roomId,onResult)

    }

    override fun observeErase(): Flow<List<String>> = callbackFlow {
        socketManager.observeErase {
            try {
                trySend(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        awaitClose()
    }

    override fun observeIncomingPath(): Flow<DrawnPath> = callbackFlow {
        socketManager.observeDrawings {
            try {
                val dto = Json.decodeFromString<DrawnPathDTO>(it)
                val path = dto.toDrawnPath()
                println("Data parsed to DrawPath Obj")
                trySend(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        awaitClose()
    }


    override suspend fun leaveRoom(roomId: String, onResult: (Boolean) -> Unit) {
        socketManager.leaveRoom(roomId) {
            onResult(it == "error")
        }

    }



}
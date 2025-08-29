package org.example.whiteboard.data.remote

import kotlinx.serialization.json.JsonObject

expect class SocketManager() {

    fun connect(onSuccess: () -> Unit)

    fun joinRoom(roomId: String, userId: String, onResult: (Boolean, String) -> Unit)

    fun leaveRoom(roomId: String, onResult: (String) -> Unit)

    fun emitPath(data: JsonObject)

    fun observeDrawings(onReceived: (String) -> Unit)

    fun disconnect()

    fun isConnected(): Boolean

    fun erase(pathsIds: List<String>, roomId: String, onResult: (Boolean, String) -> Unit)

    fun observeErase(onReceived: (ids: List<String>) -> Unit)

}
package org.example.whiteboard.data.remote

import kotlinx.serialization.json.JsonObject
import org.example.whiteboard.domain.model.DrawnPath

expect class SocketManager() {

    fun connect(onSuccess: () -> Unit)

    fun joinRoom(roomId: String, onResult:(Boolean, String)->Unit)

    fun leaveRoom(roomId: String, onResult:(String)->Unit)

    fun emitPath(data: JsonObject)

    fun onDraw(onReceived: (String) -> Unit)

    fun disconnect()

    fun isConnected(): Boolean

}
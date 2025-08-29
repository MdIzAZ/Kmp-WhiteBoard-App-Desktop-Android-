package org.example.whiteboard.data.remote

import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.IO.socket
import io.socket.client.Socket
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SocketManager actual constructor() {

//        private val socket: Socket = IO.socket("ws://localhost:8000/")
    private val socket: Socket = IO.socket("wss://whiteboard-backend-vcgb.onrender.com")

    actual fun connect(onSuccess: () -> Unit) {
        println("Desktop : Connect Called")

        socket.off(Socket.EVENT_CONNECT)

        socket.on(Socket.EVENT_CONNECT) {
            println("Desktop : Socket Connected")
            onSuccess()
        }

        try {
            socket.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    actual fun joinRoom(roomId: String, userId: String, onResult: (Boolean, String) -> Unit) {
        try {
            socket.emit("join-room", roomId, userId, Ack { args ->
                if (args.isNotEmpty()) {
                    val response = args[0].toString()
                    if (response == "joined") {
                        onResult(true, "Successfully joined room")
                    } else {
                        onResult(false, response)
                    }
                } else {
                    onResult(false, "No response from server")
                }

            })

            socket.off("join-room")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    actual fun emitPath(data: JsonObject) {
        try {
            val jsCompatibleData = data.toJsCompatible() as Map<*, *>
            socket.emit("draw", jsCompatibleData)
            println("Path Emitted")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun onDraw(onReceived: (String) -> Unit) {
        try {
            socket.on("draw") {
                println("Json Obj Received in Socket Manager")
                val rawJson = it[0].toString()
                onReceived(rawJson)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    actual fun leaveRoom(roomId: String, onResult: (String) -> Unit) {
        try {
            socket.emit("leave-room", roomId, Ack { args ->
                val status = args[0] as? String ?: "error"
                onResult(status)
            })
        } catch (e: Exception) {
            println(e)
            e.printStackTrace()
            onResult("error")
        }

    }

    actual fun disconnect() {
        socket.disconnect()
        println("Desktop : Disconnected")
    }

    actual fun isConnected(): Boolean {
        return socket.connected()
    }


}


private fun JsonElement.toJsCompatible(): Any? {
    return when (this) {
        is JsonPrimitive -> {
            when {
                this.isString -> this.content
                this.booleanOrNull != null -> this.boolean
                this.intOrNull != null -> this.int
                this.longOrNull != null -> this.long
                this.floatOrNull != null -> this.float
                this.doubleOrNull != null -> this.double
                else -> this.content
            }
        }

        is JsonObject -> this.mapValues { it.value.toJsCompatible() }
        is JsonArray -> this.map { it.toJsCompatible() }
        else -> null
    }
}
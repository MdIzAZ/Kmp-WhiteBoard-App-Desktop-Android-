package org.example.whiteboard.data.remote

import android.util.Log
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
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
import org.json.JSONArray
import org.json.JSONObject

actual class SocketManager actual constructor() {

    //        private val socket: Socket = IO.socket("ws://localhost:8000/")
    private val socket: Socket = IO.socket("wss://whiteboard-backend-vcgb.onrender.com")


    actual fun connect(onSuccess: () -> Unit) {
        Log.d("izaz", "Connect Called")

        socket.off(Socket.EVENT_CONNECT)

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("izaz", "Socket Connected")
            onSuccess()
        }

        try {
            socket.connect()
        } catch (e: Exception) {
            Log.e("izaz", "${e.message ?: e}")
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
        } catch (e: Exception) {
            Log.d("izaz", e.message ?: "Unknown error")
        }

    }

    actual fun emitPath(data: JsonObject) {
//        try {
//            val jsCompatibleData = data.toJsCompatible() as Map<*, *>
//            socket.emit("draw", jsCompatibleData)
//            println("Path Emitted")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        try {
            // Convert to JSON string
            val jsonString = Json.encodeToString(JsonObject.serializer(), data)
            // Wrap in JSONObject so Socket.IO treats it as a JSON object
            val jsonObject = org.json.JSONObject(jsonString)

            socket.emit("draw", jsonObject)
            println("Path Emitted: $jsonString")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    actual fun observeDrawings(onReceived: (String) -> Unit) {
        try {
            socket.on("draw") {
                Log.d("izaz", "Json Obj Received in Socket Manager")
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
            Log.d("izaz", "$e")

            e.printStackTrace()
            onResult("error")
        }

    }

    actual fun disconnect() {
        socket.disconnect()
    }

    actual fun isConnected(): Boolean {
        return socket.connected()
    }


    actual fun erase(pathsIds: List<String>, roomId: String, onResult: (Boolean, String) -> Unit) {
        try {

            val data = JSONObject().apply {
                put("pathsIds", JSONArray(pathsIds))
                put("roomId", roomId)
            }

            socket.emit("erase", data)

            println("Erased: $pathsIds")
            onResult(true, "Erased")
        } catch (e: Exception) {
            println(e.message ?: e)
            onResult(false, "Failed to erase")
        }

    }

    actual fun observeErase(onReceived: (List<String>) -> Unit) {
        try {
            socket.on("erase") { args ->
                val rawJson = args[0].toString()
                println("Erase event received: $rawJson")
                try {
                    val ids = Json.decodeFromString(
                        ListSerializer(String.serializer()),
                        rawJson
                    )
                    onReceived(ids)
                } catch (e: Exception) {
                    println("Failed to parse erase event: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("Socket error in observeErase: ${e.message}")
        }
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
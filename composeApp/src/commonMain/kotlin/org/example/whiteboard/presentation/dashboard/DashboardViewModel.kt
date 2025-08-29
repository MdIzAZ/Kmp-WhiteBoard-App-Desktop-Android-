package org.example.whiteboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.example.whiteboard.Information
import org.example.whiteboard.domain.repo.PathRepo
import org.example.whiteboard.domain.repo.RemoteDbRepo
import org.example.whiteboard.domain.repo.RoomRepo
import org.example.whiteboard.domain.repo.SettingsRepo
import org.example.whiteboard.domain.repo.WhiteboardRepo
import java.util.UUID
import kotlin.io.encoding.Base64

class DashboardViewModel(
    private val whiteboardRepo: WhiteboardRepo,
    private val roomRepo: RoomRepo,
    private val pathRepo: PathRepo,
    private val settingsRepo: SettingsRepo,
    private val remoteDbRepo: RemoteDbRepo
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        whiteboardRepo.getAllWhiteboards()
    ) { state, whiteboards ->

        state.copy(whiteboards = whiteboards)

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardState()
    )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepo.getAccessToken().collectLatest { token ->
                println("Token in Dashboard VM: $token")
                val userId = extractUserIdFromToken(token)
                userId?.let {
                    println("Extracted User ID: $it")
                    Information.userId = it
                }
            }
        }

    }

    fun createRoom(onSuccess: (String, Boolean) -> Unit) {
        val roomId = UUID.randomUUID().toString()
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                roomRepo.joinRoom(roomId, Information.userId) {
                    viewModelScope.launch(Dispatchers.Main) {
                        onSuccess(roomId, it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun joinRoom(roomId: String, onSuccess: (Boolean) -> Unit) {
        println("Join Room Called")
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                roomRepo.joinRoom(roomId, Information.userId) {
                    println("Room Joined: Viewmodel")
                    viewModelScope.launch(Dispatchers.Main) {
                        onSuccess(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    fun logOut() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                settingsRepo.saveAccessToken("")
                whiteboardRepo.deleteAllWhiteboards()
                pathRepo.deleteAllPaths()

            } catch (e: Exception) {
                println(e)
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }

    }


    fun renameWhiteboard(id: Long, newName: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                whiteboardRepo.renameWhiteboard(id, newName)
            } catch (e: Exception) {
                println(e.message ?: "Unknown Error")
            } finally {
                _state.update { it.copy(isLoading = false) }

            }
        }

    }

    fun deleteWhiteboard(id: Long, roomId: String?) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                roomId?.let {
                    remoteDbRepo.deleteRoom(roomId) {
                        if (it) {
                            whiteboardRepo.deleteWhiteboard(id)
                            pathRepo.deletePathsOfWhiteboard(id)
                            showToast("Whiteboard deleted successfully")
                        } else {
                            showToast("Failed to delete room from server")
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message ?: "Unknown Error")
            } finally {
                _state.update { it.copy(isLoading = false) }

            }
        }
    }


    fun showToast(message: String) {
        _state.update { it.copy(toastMessage = message) }
    }

    fun clearToast() {
        _state.update { it.copy(toastMessage = null) }
    }


}


private fun base64UrlDecode(data: String): ByteArray {
    var base64 = data.replace('-', '+').replace('_', '/')
    while (base64.length % 4 != 0) {
        base64 += '='
    }
    return Base64.decode(base64)
}

fun extractUserIdFromToken(token: String): String? {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) return null
        val payloadBytes = base64UrlDecode(parts[1])
        val payloadJson = payloadBytes.decodeToString()
        val jsonElement = Json.parseToJsonElement(payloadJson)
        jsonElement.jsonObject["userId"]?.jsonPrimitive?.content
    } catch (e: Exception) {
        null
    }
}
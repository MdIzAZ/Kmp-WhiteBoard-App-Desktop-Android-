package org.example.whiteboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.whiteboard.RoomId
import org.example.whiteboard.domain.repo.RoomRepo
import org.example.whiteboard.domain.repo.SettingsRepo
import org.example.whiteboard.domain.repo.WhiteboardRepo
import java.util.UUID

class DashboardViewModel(
    private val whiteboardRepo: WhiteboardRepo,
    private val roomRepo: RoomRepo,
    private val settingsRepo: SettingsRepo
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


    fun createRoom(onSuccess: (String, Boolean) -> Unit) {
        val roomId = UUID.randomUUID().toString()
        RoomId.roomId = roomId
        viewModelScope.launch {
            try {
                roomRepo.joinRoom(roomId) {
                    viewModelScope.launch(Dispatchers.Main) {
                        onSuccess(roomId, it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun joinRoom(roomId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                roomRepo.joinRoom(roomId) {
                    println("Room Joined: Viewmodel")
                    viewModelScope.launch(Dispatchers.Main) {
                        onSuccess(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun logOut() {
        viewModelScope.launch {
            try {
                settingsRepo.saveAuthToken("")
            } catch (e: Exception) {
                println(e)
            }
        }

    }


}
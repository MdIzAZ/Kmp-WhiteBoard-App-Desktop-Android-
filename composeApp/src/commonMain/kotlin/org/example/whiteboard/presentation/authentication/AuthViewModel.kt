package org.example.whiteboard.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.whiteboard.data.remote.dto.AuthRequest
import org.example.whiteboard.domain.repo.AuthRepo
import org.example.whiteboard.domain.repo.SettingsRepo

class AuthViewModel(
    private val authRepo: AuthRepo,
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state.asStateFlow()


    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            AuthScreenEvent.OnBtnClick -> {
                when (state.value.authMode) {
                    AuthMode.LOGIN -> login()
                    AuthMode.REGISTER -> register()
                }
            }

            is AuthScreenEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }

            is AuthScreenEvent.OnUsernameChange -> {
                _state.update { it.copy(username = event.userName) }
            }

            AuthScreenEvent.ToggleAuthModeClick -> {
                _state.update {
                    it.copy(authMode = if (state.value.authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN)
                }
            }
        }
    }


    private fun login() {
        val authRequest = AuthRequest(
            username = state.value.username,
            password = state.value.password
        )

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val res = authRepo.login(authRequest)
                println(res)
                saveAuthToken(res?.token)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                println(e)
                _state.update { it.copy(isLoading = false) }
            }
        }

    }


    private fun register() {
        val authRequest = AuthRequest(
            username = state.value.username,
            password = state.value.password
        )

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val res = authRepo.register(authRequest)
                println(res)
                saveAuthToken(res?.token)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                println(e)
                _state.update { it.copy(isLoading = false) }
            }
        }

    }


    private fun saveAuthToken(token: String?) {
        viewModelScope.launch {
            try {
                settingsRepo.saveAuthToken(token ?: "")
            } catch (e: Exception) {
                println(e)
            }
        }

    }

}
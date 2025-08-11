package org.example.whiteboard.presentation.authentication

data class AuthScreenState(
    val isLoading: Boolean = false,
    val authMode: AuthMode = AuthMode.REGISTER,
    val username: String = "",
    val password: String = "",
)

enum class AuthMode {
    LOGIN, REGISTER
}

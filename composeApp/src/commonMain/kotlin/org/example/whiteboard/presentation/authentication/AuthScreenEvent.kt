package org.example.whiteboard.presentation.authentication

sealed class AuthScreenEvent{

    data class OnUsernameChange(val userName: String): AuthScreenEvent()

    data class OnPasswordChange(val password: String): AuthScreenEvent()

    data object OnBtnClick: AuthScreenEvent()

    data object ToggleAuthModeClick: AuthScreenEvent()


}



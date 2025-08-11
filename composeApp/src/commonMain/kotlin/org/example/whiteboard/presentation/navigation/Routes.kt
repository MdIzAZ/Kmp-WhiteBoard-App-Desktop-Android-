package org.example.whiteboard.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object AuthenticationScreen : Routes()

    @Serializable
    data class WhiteboardScreen(val whiteboardId: Long?, val roomId: String?) : Routes()

    @Serializable
    data object DashboardScreen : Routes()

    @Serializable
    data object SettingsScreen : Routes()


}
package org.example.whiteboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.whiteboard.domain.model.ColorScheme
import org.example.whiteboard.presentation.authentication.AuthScreen
import org.example.whiteboard.presentation.authentication.AuthViewModel
import org.example.whiteboard.presentation.dashboard.DashboardScreen
import org.example.whiteboard.presentation.dashboard.DashboardViewModel
import org.example.whiteboard.presentation.setting_screen.SettingScreen
import org.example.whiteboard.presentation.whiteboard.WhiteBoardScreen
import org.example.whiteboard.presentation.whiteboard.WhiteBoardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    token: String?,
    currentColorScheme: ColorScheme,
    onThemeSelected: (ColorScheme) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = if (token.isNullOrBlank()) Routes.AuthenticationScreen else Routes.DashboardScreen
    ) {

        composable<Routes.AuthenticationScreen> {

            val viewmodel = koinViewModel<AuthViewModel>()
            val state by viewmodel.state.collectAsStateWithLifecycle()

            AuthScreen(
                state = state,
                onEvent = viewmodel::onEvent
            )
        }

        composable<Routes.DashboardScreen> {

            val viewModel = koinViewModel<DashboardViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DashboardScreen(
                state = state,
                onSettingIconClick = { navController.navigate(Routes.SettingsScreen) },
                onLogOutClick = { viewModel.logOut() },
                onCreateWhiteboardClick = {
                    navController.navigate(
                        Routes.WhiteboardScreen(whiteboardId = null, roomId = null)
                    )
                },
                onWhiteboardClick = { id, roomId ->
                    navController.navigate(
                        Routes.WhiteboardScreen(whiteboardId = id, roomId = roomId)
                    )
                },
                onJoinWhiteboardClick = {
                    viewModel.joinRoom(it) { isSuccess ->
                        if (!isSuccess) return@joinRoom
                        navController.navigate(
                            Routes.WhiteboardScreen(whiteboardId = null, roomId = it)
                        )
                    }
                },
                createSharedRoom = {
                    viewModel.createRoom { roomId, isSuccess ->
                        if (!isSuccess) return@createRoom
                        navController.navigate(
                            Routes.WhiteboardScreen(whiteboardId = null, roomId = roomId)
                        )
                    }
                },
                onRenameClick = viewModel::renameWhiteboard,
                onDeleteClick = viewModel::deleteWhiteboard,
                onClearToast = viewModel::clearToast
            )
        }

        composable<Routes.WhiteboardScreen>() {

            val viewModel = koinViewModel<WhiteBoardViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            WhiteBoardScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onHomeIconClick = {
                    viewModel.navigateBack { navController.navigateUp() }
                },
                onClearToast = viewModel::clearToast
            )
        }

        composable<Routes.SettingsScreen>() {
            SettingScreen(
                currentColorScheme = currentColorScheme,
                onThemeSelected = onThemeSelected,
                onBack = { navController.navigateUp() },
            )
        }
    }

}
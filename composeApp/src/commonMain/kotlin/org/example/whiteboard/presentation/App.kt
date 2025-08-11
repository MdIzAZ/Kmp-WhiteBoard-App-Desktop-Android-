package org.example.whiteboard.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.example.whiteboard.domain.model.ColorScheme
import org.example.whiteboard.presentation.navigation.NavGraph
import org.example.whiteboard.presentation.setting_screen.SettingsViewModel
import org.example.whiteboard.presentation.theme.WhiteboardTheme
import org.example.whiteboard.presentation.whiteboard.WhiteBoardViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {

    val settingsViewModel = koinViewModel<SettingsViewModel>()

    val currentColorScheme by settingsViewModel.currentColorScheme.collectAsStateWithLifecycle()
    val token by settingsViewModel.token.collectAsStateWithLifecycle()

    val isDarkTheme = when(currentColorScheme) {
        ColorScheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
        ColorScheme.LIGHT -> false
        ColorScheme.DARK -> true
    }



    WhiteboardTheme(
        darkTheme = isDarkTheme
    ) {

        val navController = rememberNavController()

            NavGraph(
                modifier = Modifier,
                navController = navController,
                currentColorScheme = currentColorScheme,
                token = token,
                onThemeSelected = { settingsViewModel.saveColorScheme(it) }
            )

    }


}
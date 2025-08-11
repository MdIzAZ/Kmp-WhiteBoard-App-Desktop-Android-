package org.example.whiteboard.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
actual fun rememberScreenSizeInfo(): ScreenSizeInfo {
    val config = LocalConfiguration.current

    return remember(config) {
        ScreenSizeInfo(
            width = config.screenWidthDp.dp,
            height = config.screenHeightDp.dp
        )
    }
}
package org.example.whiteboard.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
expect fun rememberScreenSizeInfo() : ScreenSizeInfo


data class ScreenSizeInfo(
    val width: Dp,
    val height: Dp
)

fun ScreenSizeInfo.getUiType(): ScreenType {
    return when(width) {
        in 0.dp..600.dp -> ScreenType.COMPACT
        in 600.dp..840.dp -> ScreenType.MEDIUM
        else -> ScreenType.EXPANDED
    }
}

enum class ScreenType {
    COMPACT,
    MEDIUM,
    EXPANDED
}
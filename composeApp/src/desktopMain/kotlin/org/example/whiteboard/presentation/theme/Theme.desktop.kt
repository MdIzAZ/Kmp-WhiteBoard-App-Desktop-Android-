package org.example.whiteboard.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.ui.theme.AppTypography

@Composable
actual fun WhiteboardTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if(darkTheme) darkScheme else lightScheme,
        typography = AppTypography,
        content = content
    )

}
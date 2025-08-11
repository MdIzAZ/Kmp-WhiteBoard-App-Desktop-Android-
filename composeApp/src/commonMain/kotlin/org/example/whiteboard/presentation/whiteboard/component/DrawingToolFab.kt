package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.DrawingTool
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawingToolFab(
    modifier: Modifier,
    isVisible: Boolean,
    selectedTool: DrawingTool,
    onClick: () -> Unit,
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = fadeIn() + expandIn(),
        exit = shrinkOut() + fadeOut() ,
    ) {
        FloatingActionButton(
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(selectedTool.iconRes),
                contentDescription = selectedTool.name,
                tint = if (selectedTool.isColored) Color.Unspecified
                else LocalContentColor.current
            )
        }
    }

}
package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.DrawingTool

@Composable
fun DrawingToolsCardHorizontal(
    modifier: Modifier,
    selectedTool: DrawingTool,
    onToolClick: (DrawingTool) -> Unit,
    isToolBoxVisible: Boolean,
    onToolBoxCloseClick: () -> Unit,
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = isToolBoxVisible,
        enter = slideInHorizontally ( tween(500) ) { fullWidth -> fullWidth  },
        exit = slideOutHorizontally ( tween(500) ) { fullWidth -> fullWidth  },
    ) {

        ElevatedCard {

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(DrawingTool.entries) {
                        DrawingToolItem(
                            drawingTool = it,
                            isSelected = selectedTool == it,
                            onToolClick = { onToolClick(it) },
                        )
                    }
                }

                FilledIconButton( onClick = { onToolBoxCloseClick() } ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }


}

@Composable
fun DrawingToolsCardVertical(
    modifier: Modifier,
    selectedTool: DrawingTool,
    onToolClick: (DrawingTool) -> Unit,
    isToolBoxVisible: Boolean,
    onToolBoxCloseClick: () -> Unit,
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = isToolBoxVisible,
        enter = slideInVertically ( tween(500) ) { fh -> fh },
        exit = slideOutHorizontally ( tween(500) ) { fh -> fh },
    ) {
        ElevatedCard {
            Column (
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                LazyColumn (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(DrawingTool.entries) {
                        DrawingToolItem(
                            drawingTool = it,
                            isSelected = selectedTool == it,
                            onToolClick = { onToolClick(it) },
                        )
                    }
                }

                FilledIconButton( onClick = { onToolBoxCloseClick() } ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }


}


@Composable
private fun DrawingToolItem(
    drawingTool: DrawingTool,
    isSelected: Boolean,
    onToolClick: () -> Unit,
) {

    Column( horizontalAlignment = Alignment.CenterHorizontally ) {
        IconButton(onClick = { onToolClick() }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(drawingTool.iconRes),
                contentDescription = drawingTool.name,
                tint = if (drawingTool.isColored) Color.Unspecified
                else LocalContentColor.current
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .background(LocalContentColor.current)
                    .size(25.dp, 1.dp)
            )
        }

    }
}
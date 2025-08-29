package org.example.whiteboard.presentation.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_create_room
import whiteboard.composeapp.generated.resources.ic_join
import whiteboard.composeapp.generated.resources.ic_person

@Composable
fun VerticalFabSection(
    modifier: Modifier,
    onCreateWhiteboard: () -> Unit,
    onJoinWhiteboard: () -> Unit,
    createSharedRoom: () -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {

            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    FavOption(
                        title = "Join Room",
                        icon = painterResource(Res.drawable.ic_join),
                        onOptionClick = {
                            onJoinWhiteboard()
                            expanded = false
                        }
                    )

                    FavOption(
                        title = "Create Room",
                        icon = painterResource(Res.drawable.ic_create_room),
                        onOptionClick = {
                            createSharedRoom()
                            expanded = false
                        }
                    )

                    FavOption(
                        title = "Personal",
                        icon = painterResource(Res.drawable.ic_person),
                        onOptionClick = {
                            onCreateWhiteboard()
                            expanded = false
                        }
                    )


                }
            }


            FloatingActionButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Toggle"
                )
            }
        }
    }

}


@Composable
fun FavOption(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    onOptionClick: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color = Color.White,
            modifier = Modifier.padding(end = 2.dp)
                .background(
                    color = Color.Gray.copy(alpha = .9f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp)
        )

        FloatingActionButton(
            onClick = onOptionClick
        ) {
            Icon(
                painter = icon,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified,
                contentDescription = "Join"
            )
        }
    }

}



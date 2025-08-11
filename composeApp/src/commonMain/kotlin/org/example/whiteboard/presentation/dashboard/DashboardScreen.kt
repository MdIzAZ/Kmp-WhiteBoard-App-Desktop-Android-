package org.example.whiteboard.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.Whiteboard
import org.example.whiteboard.presentation.dashboard.components.JoinRoomDialog
import org.example.whiteboard.presentation.dashboard.components.LogOutAlertDialog
import org.example.whiteboard.presentation.dashboard.components.VerticalFabSection
import org.example.whiteboard.presentation.dashboard.components.WhiteboardItemCard
import org.jetbrains.compose.resources.painterResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_logout


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onSettingIconClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onCreateWhiteboardClick: () -> Unit,
    onWhiteboardClick: (Long, String?) -> Unit,
    onJoinWhiteboardClick: (roomId: String) -> Unit,
    createSharedRoom: () -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    var shouldShowAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DashboardTopBar(
                onSettingIconClick = onSettingIconClick,
                onLogOutIconClick = { shouldShowAlertDialog = true }
            )
        }
    ) { ip ->
        Box(
            modifier = modifier.fillMaxSize().padding(ip)
        ) {
            Column {

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.whiteboards) { whiteboard ->
                        WhiteboardItemCard(
                            modifier = Modifier.padding(8.dp).clickable {
                                whiteboard.id?.let {
                                    onWhiteboardClick(whiteboard.id, whiteboard.roomId)
                                }
                            },
                            whiteboard = Whiteboard(
                                id = whiteboard.id,
                                name = whiteboard.name,
                                lastEdited = whiteboard.lastEdited,
                                roomId = whiteboard.roomId,
                                canvasColor = whiteboard.canvasColor
                            ),
                            onRenameClick = { },
                            onDeleteClick = { }
                        )
                    }

                }
            }

            if (expanded) {
                JoinRoomDialog(
                    onJoin = { onJoinWhiteboardClick(it) },
                    onDismiss = { expanded = false }
                )
            }

            if (shouldShowAlertDialog) {
                LogOutAlertDialog(
                    onDismiss = { shouldShowAlertDialog = false },
                    onLogOut = { onLogOutClick() }
                )
            }

            VerticalFabSection(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onJoinWhiteboard = {
                    expanded = true
                },
                onCreateWhiteboard = { onCreateWhiteboardClick() },
                createSharedRoom = { createSharedRoom() }
            )

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    modifier: Modifier = Modifier,
    onSettingIconClick: () -> Unit,
    onLogOutIconClick: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        title = { Text("Dashboard") },
        actions = {
            IconButton(
                onClick = onSettingIconClick,
                content = {
                    Icon(Icons.Default.Settings, "Settings")
                }
            )

            IconButton(
                onClick = onLogOutIconClick,
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_logout),
//                        imageVector = Icons.Default.Settings,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Log Out"
                    )
                }
            )
        }
    )

}
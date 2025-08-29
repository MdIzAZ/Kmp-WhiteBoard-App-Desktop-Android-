package org.example.whiteboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.whiteboard.domain.model.Whiteboard
import org.example.whiteboard.presentation.dashboard.components.InputDialog
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
    onRenameClick: (id: Long, newName: String) -> Unit,
    onDeleteClick: (id:Long, roomId:String?) -> Unit,
    onClearToast:()->Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var currentWhiteboardId by remember { mutableLongStateOf(-1L) }
    var inputDialogCurrentType by remember { mutableStateOf<InputDialogCurrentType?>(null) }

    var shouldShowAlertDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onClearToast()
        }
    }

    Scaffold(
        topBar = {
            DashboardTopBar(
                onSettingIconClick = onSettingIconClick,
                onLogOutIconClick = { shouldShowAlertDialog = true }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                            onRenameClick = {
                                currentWhiteboardId = whiteboard.id!!
                                inputDialogCurrentType = InputDialogCurrentType.RENAME_WHITEBOARD
                                expanded = true
                            },
                            onDeleteClick = { onDeleteClick(whiteboard.id!!, whiteboard.roomId) }
                        )
                    }

                }
            }

            if (expanded) {
                when (inputDialogCurrentType) {
                    InputDialogCurrentType.JOIN_ROOM -> {
                        InputDialog(
                            title = "Join Whiteboard",
                            label = "Enter Room ID",
                            confirmBtnTxt = "Join Room",
                            dismissBtnTxt = "Cancel",
                            onConfirm = {
                                onJoinWhiteboardClick(it)
                                inputDialogCurrentType = null
                                expanded = false
                            },
                            onDismiss = { expanded = false }
                        )
                    }

                    InputDialogCurrentType.RENAME_WHITEBOARD -> {
                        InputDialog(
                            title = "Rename Whiteboard",
                            label = "Enter New Name",
                            confirmBtnTxt = "Rename",
                            dismissBtnTxt = "Cancel",
                            onConfirm = {
                                onRenameClick(currentWhiteboardId, it)
                                inputDialogCurrentType = null
                                expanded = false
                            },
                            onDismiss = { expanded = false }
                        )
                    }

                    null -> {}
                }

            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .wrapContentSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(24.dp).size(80.dp).align(Alignment.Center)
                    )
                }
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
                    inputDialogCurrentType = InputDialogCurrentType.JOIN_ROOM
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


enum class InputDialogCurrentType {
    JOIN_ROOM, RENAME_WHITEBOARD
}



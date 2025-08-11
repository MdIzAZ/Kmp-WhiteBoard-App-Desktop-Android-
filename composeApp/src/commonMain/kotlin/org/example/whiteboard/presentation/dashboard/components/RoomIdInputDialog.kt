package org.example.whiteboard.presentation.dashboard.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties

@Composable
fun JoinRoomDialog(
    onDismiss: () -> Unit,
    onJoin: (roomId: String) -> Unit
) {

    var roomId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = roomId.isNotBlank(),
                onClick = {
                    onJoin(roomId)
                    onDismiss()
                },
                content = {
                    Text("Join")
                }
            )

        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text("Dismiss")
                }
            )
        },
        title = {
            Text(text = "Join Whiteboard")
        },
        text = {
            OutlinedTextField(
                value = roomId,
                onValueChange = { roomId = it },
                label = { Text("Enter Room ID") },
                singleLine = true
            )
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}

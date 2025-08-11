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
import kotlinx.coroutines.NonCancellable.onJoin
import kotlinx.serialization.json.JsonNull.content
import org.example.whiteboard.RoomId.roomId

@Composable
fun LogOutAlertDialog(
    onDismiss: () -> Unit,
    onLogOut: () -> Unit
) {


    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onLogOut,
                content = {
                    Text("Log Out")
                }
            )

        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text("Cancel")
                }
            )
        },
        title = {
            Text(text = "Are you sure? ")
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}

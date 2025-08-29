package org.example.whiteboard.presentation.dashboard.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

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
            Text(text = "Are you sure? \nAll Local Whiteboards will be erased! ")
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    )
}

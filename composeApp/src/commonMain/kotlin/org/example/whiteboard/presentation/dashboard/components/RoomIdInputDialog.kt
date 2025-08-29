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
fun InputDialog(
    title: String,
    label: String,
    confirmBtnTxt: String = "Confirm",
    dismissBtnTxt: String = "Dismiss",
    onDismiss: () -> Unit,
    onConfirm: (roomId: String) -> Unit
) {

    var txtFieldValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = txtFieldValue.isNotBlank(),
                onClick = {
                    onConfirm(txtFieldValue)
                    onDismiss()
                },
                content = {
                    Text(confirmBtnTxt)
                }
            )

        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text(dismissBtnTxt)
                }
            )
        },
        title = {
            Text(text = title)
        },
        text = {
            OutlinedTextField(
                value = txtFieldValue,
                onValueChange = { txtFieldValue = it },
                label = { Text(label) },
                singleLine = true
            )
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}

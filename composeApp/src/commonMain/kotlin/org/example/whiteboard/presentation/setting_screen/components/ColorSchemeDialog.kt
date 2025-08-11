package org.example.whiteboard.presentation.setting_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.ColorScheme


@Composable
fun ColorSchemeDialog(
    modifier: Modifier,
    currentColorScheme: ColorScheme,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onThemeSelected: (ColorScheme) -> Unit
) {

    var selectedScheme by rememberSaveable{ mutableStateOf(currentColorScheme) }

    if (!isOpen) return
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "New Color Scheme") },
        text = {
            Column {
                ColorScheme.entries.forEach { colorScheme ->
                    LabeledRadioButton(
                        label = colorScheme.label,
                        selected = selectedScheme == colorScheme,
                        onClick = {
                            selectedScheme = colorScheme
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                content = { Text(text = "OK") },
                onClick = {
                    onThemeSelected(selectedScheme)
                    onDismissRequest()
                },
            )
        },
        dismissButton = {
            TextButton(
                content = { Text(text = "Close") },
                onClick = onDismissRequest,
            )
        },
        onDismissRequest = onDismissRequest
    )
}


@Composable
fun LabeledRadioButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )

        Text(text = label)
    }

}
package org.example.whiteboard.presentation.setting_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.whiteboard.domain.model.ColorScheme
import org.example.whiteboard.presentation.setting_screen.components.ColorSchemeDialog
import org.jetbrains.compose.resources.painterResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_theme

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    currentColorScheme: ColorScheme,
    onThemeSelected: (ColorScheme) -> Unit
) {

    var isColorSchemeDialogOpened by rememberSaveable { mutableStateOf(false) }

    ColorSchemeDialog(
        modifier = Modifier,
        currentColorScheme = currentColorScheme,
        isOpen = isColorSchemeDialogOpened,
        onDismissRequest = { isColorSchemeDialogOpened = false },
        onThemeSelected = onThemeSelected
    )

    Scaffold(
        topBar = {
            SettingsTopBar(
                onBack = onBack
            )
        }
    ) { ip ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(ip)
        ) {

            ListItem(
                modifier = Modifier.clickable { isColorSchemeDialogOpened = true },
                headlineContent = { Text("Color Scheme") },
                supportingContent = { Text(text = currentColorScheme.label) },
                leadingContent = {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(Res.drawable.ic_theme),
                        contentDescription = "Color Scheme Settings"
                    )
                }
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        title = { Text("Settings") },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                content = {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, "Back to Dashboard")
                }
            )
        }
    )

}
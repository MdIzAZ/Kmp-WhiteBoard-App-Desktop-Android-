package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_redo
import whiteboard.composeapp.generated.resources.ic_undo
import whiteboard.composeapp.generated.resources.ic_zoom_in
import whiteboard.composeapp.generated.resources.zoom_out

@Composable
fun TopBarHorizontal(
    modifier: Modifier = Modifier,
    onHomeIconClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onMenuClick: () -> Unit,
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
) {

    Row(
        modifier = modifier,
    ) {

        FilledIconButton(
            onClick = onHomeIconClick,
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Home"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledIconButton(
            onClick = onZoomInClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_zoom_in),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Zoom In"
            )
        }


        FilledIconButton(
            onClick = onZoomOutClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.zoom_out),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Zoom Out",
            )
        }

        FilledIconButton(
            onClick = onUndoClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_undo),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Undo"
            )
        }

        FilledIconButton(
            onClick = onRedoClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_redo),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Redo"
            )
        }

        FilledIconButton(
            onClick = { onMenuClick() },
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "More Options"
            )
        }


    }
}


@Composable
fun TopBarVertical(
    modifier: Modifier = Modifier,
    onHomeIconClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onMenuClick: () -> Unit,
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
) {

    Column(
        modifier = modifier,
    ) {

        FilledIconButton(
            onClick = onHomeIconClick,
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Home"
            )
        }


        FilledIconButton(
            onClick = { onMenuClick() },
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Command Palette"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledIconButton(
            onClick = onZoomInClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_zoom_in),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Zoom In",
            )
        }

        FilledIconButton(
            onClick = onZoomOutClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.zoom_out),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Zoom Out",
            )
        }

        FilledIconButton(
            onClick = onUndoClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_undo),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Undo"
            )
        }

        FilledIconButton(
            onClick = onRedoClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_redo),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp),
                contentDescription = "Redo"
            )
        }




    }
}









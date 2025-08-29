package org.example.whiteboard.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.example.whiteboard.domain.model.Whiteboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.img_canvas

@Preview
@Composable
fun WhiteboardItemCard(
    modifier: Modifier = Modifier,
    whiteboard: Whiteboard = Whiteboard(
        id = 1L,
        name = "Sample Whiteboard",
        lastEdited = LocalDate(2025, 4, 1),
        canvasColor = MaterialTheme.colorScheme.primary,
        roomId = "abcd1234"
    ),
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.img_canvas),
            contentDescription = null
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f).padding(8.dp),
                text = whiteboard.name ,
                maxLines = 2
            )
            Box {
                IconButton(
                    content = {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
                        )
                    },
                    onClick = { isExpanded = true },
                )

                WhiteboardCardMoreOptionsMenu(
                    isExpanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    onRenameClick = {
                        onRenameClick()
                        isExpanded = false
                    },
                    onDeleteClick = {
                        onDeleteClick()
                        isExpanded = false
                    }
                )
            }

        }

        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            text = "Last Edited: ${whiteboard.lastEdited}",
            style = MaterialTheme.typography.labelSmall
        )
    }

}
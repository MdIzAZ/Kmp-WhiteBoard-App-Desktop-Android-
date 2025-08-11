package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import whiteboard.composeapp.generated.resources.Res
import whiteboard.composeapp.generated.resources.ic_transparent_bg
import whiteboard.composeapp.generated.resources.img_color_wheel

@Composable
fun ColorSection(
    selectionTitle: String,
    colors: List<Color>,
    selectedColor: Color,
    onColorClick: (Color) -> Unit,
    onColorWheelClick: () -> Unit,
    isFillColors: Boolean = false
) {
    Column() {
        Text(
            text = selectionTitle,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isFillColors) {
                item {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .border(
                                width = 1.dp,
                                color = if (selectedColor == Color.Transparent) MaterialTheme.colorScheme.primary
                                else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .clip( CircleShape)
                            .clickable { onColorClick(Color.Transparent) },
                        painter = painterResource(Res.drawable.ic_transparent_bg),
                        contentDescription = "set transparent background",
                        tint = Color.Unspecified
                    )
                }
            }

            items(colors) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            width = 1.dp,
                            color = if (selectedColor == it) MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                        .background(it, CircleShape)
                        .clickable { onColorClick(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(modifier = Modifier.size(30.dp), onClick = { onColorWheelClick() }) {
                    Icon(
                        painter = painterResource(Res.drawable.img_color_wheel),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }


}
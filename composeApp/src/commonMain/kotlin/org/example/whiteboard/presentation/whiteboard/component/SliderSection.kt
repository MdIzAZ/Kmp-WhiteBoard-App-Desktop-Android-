package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SliderSection(
    modifier: Modifier = Modifier,
    title: String,
    sliderValue: Float,
    onSliderWidthValueChange: (Float) -> Unit,
    sliderValueRange: ClosedFloatingPointRange<Float>
) {
    Column() {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Slider(
                modifier = Modifier.weight(1f),
                value = sliderValue,
                onValueChange = onSliderWidthValueChange,
                valueRange = sliderValueRange
            )

            Text("${sliderValue.toInt()}")
        }

    }

}
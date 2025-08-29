package org.example.whiteboard.presentation.whiteboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min
import kotlin.math.roundToInt


@Composable
fun SliderBtn(
    modifier: Modifier = Modifier,
    sliderOffset: Float,
    onSliderOffsetChange: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    Box(
        modifier = modifier
            .offset {
                IntOffset(0, sliderOffset.roundToInt())
            }
            .width(40.dp)
            .height(80.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onSliderOffsetChange(change, dragAmount)
                    }
                )
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(8.dp))

            for (i in 0..6) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}


@Preview
@Composable
fun SliderPreview() {
    SliderBtn(
        sliderOffset = 100f,
        onSliderOffsetChange = {_,_->}
    )
}
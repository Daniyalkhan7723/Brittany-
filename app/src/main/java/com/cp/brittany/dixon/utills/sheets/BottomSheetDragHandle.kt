package com.cp.brittany.dixon.utills.sheets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.cp.brittany.dixon.utills.sdp

/**
 * A bottom sheet drag handle will be displayed as a rounded rectangle.
 */
@Composable
fun CoreBottomSheetDragHandle(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray.copy(alpha = 0.3f),
    height: Dp = 24.sdp,
    barWidth: Dp = 32.sdp,
    barHeight: Dp = 4.sdp,
) {
    Spacer(
        modifier = modifier
            .drawBehind {
                val barWidthPx = barWidth.toPx()
                val barHeightPx = barHeight.toPx()
                val x = size.width / 2 - barWidthPx / 2
                val y = size.height / 2 - barHeightPx / 2
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(barWidthPx, barHeightPx),
                    cornerRadius = CornerRadius(barHeightPx / 2)
                )
            }
            .fillMaxWidth()
            .height(height)
    )
}

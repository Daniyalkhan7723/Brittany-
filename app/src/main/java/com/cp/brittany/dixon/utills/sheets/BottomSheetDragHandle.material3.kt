package com.cp.brittany.dixon.utills.sheets

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.cp.brittany.dixon.utills.sdp

/**
 * A bottom sheet drag handle will be displayed as a rounded rectangle.
 */
@Composable
fun BottomSheetDragHandle(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
        alpha = 0.4f
    ),
    height: Dp = 24.sdp,
    barWidth: Dp = 32.sdp,
    barHeight: Dp = 4.sdp,
) {
    CoreBottomSheetDragHandle(modifier, color, height, barWidth, barHeight)
}
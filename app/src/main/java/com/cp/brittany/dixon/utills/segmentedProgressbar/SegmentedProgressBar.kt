package com.cp.brittany.dixon.utills.segmentedProgressbar

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.utills.segmentedProgressbar.models.SegmentColor
import com.cp.brittany.dixon.utills.segmentedProgressbar.models.SegmentCoordinates

@Composable
fun SegmentedProgressBar(
    @IntRange(from = 1) segmentCount: Int,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0) progress: Float = 0f,
    @FloatRange(from = 0.0) spacing: Dp = 0.dp,
    @FloatRange(from = -60.0, to = 60.0) angle: Float = 0f,
    segmentColor: SegmentColor = SegmentColor(),
    progressColor: SegmentColor = SegmentColor(),
    drawSegmentsBehindProgress: Boolean = false,
    progressAnimationSpec: AnimationSpec<Float> = tween(),
    onProgressChanged: ((progress: Float, progressCoordinates: SegmentCoordinates) -> Unit)? = null,
    onProgressFinished: ((progress: Float) -> Unit)? = null,
) {
    val computer = remember { SegmentCoordinatesComputer() }
    var progressCoordinates by remember { mutableStateOf(SegmentCoordinates(0f, 0f, 0f, 0f)) }
    val spacingPx = LocalDensity.current.run { spacing.toPx() }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressAnimationSpec,
        finishedListener = onProgressFinished, label = "",
    )

    if (animatedProgress.compareTo(progress) != 0) {
        onProgressChanged?.invoke(animatedProgress, progressCoordinates)
    }

    val progressRange = 0f..segmentCount.toFloat()

    Canvas(
        modifier = modifier
            .progressSemantics(
                value = progress,
                valueRange = progressRange,
            )
            .fillMaxWidth()
            .clipToBounds(),
        onDraw = {
            progressCoordinates = computer.progressCoordinates(
                progress = animatedProgress.coerceIn(progressRange),
                segmentCount = segmentCount,
                width = size.width,
                height = size.height,
                spacing = spacingPx,
                angle = angle
            )

            (0 until segmentCount).forEach { position ->
                val segmentCoordinates = computer.segmentCoordinates(
                    position = position,
                    segmentCount = segmentCount,
                    width = size.width,
                    height = size.height,
                    spacing = spacingPx,
                    angle = angle
                )

                // Prevents drawing segment below progress to lighten drawing cycle and avoid bad alpha progress rendering
                if (drawSegmentsBehindProgress || segmentCoordinates.topRightX.compareTo(
                        progressCoordinates.topRightX
                    ) > 0
                ) {
                    drawSegment(
                        coordinates = segmentCoordinates,
                        color = segmentColor
                    )
                }
            }

            drawSegment(
                coordinates = progressCoordinates,
                color = progressColor
            )
        }
    )
}

private fun DrawScope.drawSegment(coordinates: SegmentCoordinates, color: SegmentColor) {
    val path = Path().apply {
        reset()
        moveTo(coordinates.topLeftX, 0f)
        lineTo(coordinates.topRightX, 0f)
        lineTo(coordinates.bottomRightX, size.height)
        lineTo(coordinates.bottomLeftX, size.height)
        close()
    }
    drawPath(
        path = path,
        color = color.color,
        alpha = color.alpha,
    )
}

package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp

@Composable
fun CustomArcShape(
    screenType: String,
    modifier: Modifier,
    elevation: Dp = 4.sdp,
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(color),
    navigateToVideoPlayer: () -> Unit,
    content: @Composable () -> Unit,

    ) {

    val diameter = 40.sdp
    val radiusDp = diameter / 2

    val cornerRadiusDp = 14.sdp

    val density = LocalDensity.current
    val cutoutRadius = density.run { radiusDp.toPx() }
    val cornerRadius = density.run { cornerRadiusDp.toPx() }

    val click by remember {
        mutableStateOf(Modifier.clickable {
            navigateToVideoPlayer()
        })
    }

    val shape = remember {
        GenericShape { size: Size, layoutDirection: LayoutDirection ->
            this.roundedRectanglePath(
                size = size, cornerRadius = cornerRadius, fabRadius = 55f
            )
        }
    }
    Box(contentAlignment = Alignment.TopCenter) {

        Image(
            painter = painterResource(id = R.drawable.ic_float_play),
            contentDescription = "Search",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 22.sdp, top = 263.sdp)
                .offset(y = -diameter / 5)
                .then(click)
                .size(diameter)
//                .size(38.sdp),
        )

        Surface(
            modifier = modifier,
            shape = shape,
            shadowElevation = elevation,
            color = color,
            contentColor = contentColor
        ) {
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(diameter))
                content()

            }
        }


    }
}

fun Path.roundedRectanglePath(
    size: Size,
    cornerRadius: Float,
    fabRadius: Float,
) {

    val centerX = size.width / 1.15f
    val x0 = centerX - fabRadius * 1.65f
    val y0 = 0f

    // offset of the first control point (top part)
    val topControlX = x0 + fabRadius * .5f

    // offset of the second control point (bottom part)
    val bottomControlY = y0 + fabRadius

    /*
       First curve
    */
    // set the starting point of the curve (P2)
    val firstCurveStart = Offset(x0, y0)

    // set the end point for the first curve (P3)
    val firstCurveEnd = Offset(centerX, fabRadius * 1f)

    // set the first control point (C1)
    val firstCurveControlPoint1 = Offset(
        x = topControlX, y = y0
    )

    // set the second control point (C2)
    val firstCurveControlPoint2 = Offset(
        x = x0, y = bottomControlY
    )

    /*
        Second curve
    */
    // end of first curve and start of second curve is the same (P3)
    val secondCurveStart = Offset(
        x = firstCurveEnd.x, y = firstCurveEnd.y
    )

    // end of the second curve (P4)
    val secondCurveEnd = Offset(
        x = centerX + fabRadius * 1.45f, y = 0f
    )

    // set the first control point of second curve (C4)
    val secondCurveControlPoint1 = Offset(
        x = secondCurveStart.x + fabRadius, y = bottomControlY
    )

    // set the second control point (C3)
    val secondCurveControlPoint2 = Offset(
        x = secondCurveEnd.x - fabRadius / 2, y = y0
    )


    // Top left arc
    val radius = cornerRadius * 2

    arcTo(
        rect = Rect(
            left = 0f, top = 0f, right = radius, bottom = radius
        ), startAngleDegrees = 180.0f, sweepAngleDegrees = 90.0f, forceMoveTo = false
    )



    lineTo(x = firstCurveStart.x, y = firstCurveStart.y)

    // bezier curve with (P2, C1, C2, P3)
    cubicTo(
        x1 = firstCurveControlPoint1.x,
        y1 = firstCurveControlPoint1.y,
        x2 = firstCurveControlPoint2.x,
        y2 = firstCurveControlPoint2.y,
        x3 = firstCurveEnd.x,
        y3 = firstCurveEnd.y
    )

    // bezier curve with (P3, C4, C3, P4)
    cubicTo(
        x1 = secondCurveControlPoint1.x,
        y1 = secondCurveControlPoint1.y,
        x2 = secondCurveControlPoint2.x,
        y2 = secondCurveControlPoint2.y,
        x3 = secondCurveEnd.x,
        y3 = secondCurveEnd.y
    )

//    lineTo(x = size.width - cornerRadius, y = 0f)

    // Top right arc
    arcTo(
        rect = Rect(
            left = size.width - radius, top = 0f, right = size.width, bottom = radius
        ), startAngleDegrees = -90.0f, sweepAngleDegrees = 90.0f, forceMoveTo = false
    )

//    lineTo(x = 0f + size.width, y = size.height - cornerRadius)

    // Bottom right arc
    arcTo(
        rect = Rect(
            left = size.width - radius,
            top = size.height - radius,
            right = size.width,
            bottom = size.height
        ), startAngleDegrees = 0f, sweepAngleDegrees = 90.0f, forceMoveTo = false
    )

//    lineTo(x = cornerRadius, y = size.height)

    // Bottom left arc
    arcTo(
        rect = Rect(
            left = 0f, top = size.height - radius, right = radius, bottom = size.height
        ), startAngleDegrees = 90.0f, sweepAngleDegrees = 90.0f, forceMoveTo = false
    )

//    lineTo(x = 0f, y = cornerRadius)
    close()
}


class CustomShape(private val waveCount: Int = 0) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Generic(
            path = drawCustomPath(size, waveCount)
        )
    }
}

fun drawCustomPath(size: Size, waveCount: Int): Path {
    val waveLength = size.width / (waveCount + 1)
    val waveHeight = waveLength / 5
    val gap = 3 * waveLength / 4
    return Path().apply {
        reset()
        moveTo(0f, 0f)
        arcTo(
            rect = Rect(
                topLeft = Offset(-waveLength / 4, 0f),
                bottomRight = Offset(waveLength / 4, waveHeight)
            ), startAngleDegrees = 270f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                topLeft = Offset(waveLength / 4, 0f), bottomRight = Offset(gap, waveHeight)
            ), startAngleDegrees = 180f, sweepAngleDegrees = -180f, forceMoveTo = false
        )
        for (i in 1..waveCount) {
            arcTo(
                rect = Rect(
                    topLeft = Offset(gap + waveLength * (i - 1), 0f),
                    bottomRight = Offset(gap + waveLength * (i - 1) + waveLength / 2, waveHeight)
                ), startAngleDegrees = 180f, sweepAngleDegrees = 180f, forceMoveTo = false
            )
            arcTo(
                rect = Rect(
                    topLeft = Offset(gap + waveLength * (i - 1) + waveLength / 2, 0f),
                    bottomRight = Offset(gap + waveLength * i, waveHeight)
                ), startAngleDegrees = 180f, sweepAngleDegrees = -180f, forceMoveTo = false
            )
        }
        arcTo(
            rect = Rect(
                topLeft = Offset(size.width - waveLength / 4, 0f),
                bottomRight = Offset(size.width + waveLength / 4, waveHeight)
            ), startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        lineTo(size.width, size.height)
        arcTo(
            rect = Rect(
                topLeft = Offset(size.width - waveLength / 4, size.height - waveHeight),
                bottomRight = Offset(size.width + waveLength / 4, size.height)
            ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        arcTo(
            rect = Rect(
                topLeft = Offset(
                    size.width - gap, size.height - waveHeight
                ), bottomRight = Offset(size.width - waveLength / 4, size.height)
            ), startAngleDegrees = 0f, sweepAngleDegrees = -180f, forceMoveTo = false
        )
        for (i in 1..waveCount) {
            arcTo(
                rect = Rect(
                    topLeft = Offset(
                        size.width - gap - waveLength * (i - 1) - waveLength / 2,
                        size.height - waveHeight
                    ), bottomRight = Offset(
                        size.width - gap - waveLength * (i - 1), size.height
                    )
                ), startAngleDegrees = 0f, sweepAngleDegrees = 180f, forceMoveTo = false
            )
            arcTo(
                rect = Rect(
                    topLeft = Offset(
                        size.width - gap - waveLength * i, size.height - waveHeight
                    ), bottomRight = Offset(
                        size.width - gap - waveLength * (i - 1) - waveLength / 2, size.height
                    )
                ), startAngleDegrees = 0f, sweepAngleDegrees = -180f, forceMoveTo = false
            )
        }
        arcTo(
            rect = Rect(
                topLeft = Offset(-waveLength / 4, size.height - waveHeight),
                bottomRight = Offset(waveLength / 4, size.height)
            ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        close()
    }
}


@Composable
fun AreShapeOnBorderCenterSurface(
    cornerRadius: Dp, centerCircleRadius: Dp, content: @Composable () -> Unit
) {

    val density = LocalDensity.current
    val cornerRadiusPx = density.run {
        cornerRadius.toPx()
    }
    val centerCircleRadiusPx = density.run {
        centerCircleRadius.toPx()
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = GenericShape { size: Size, _: LayoutDirection ->
                        buildCustomPath(size, cornerRadiusPx, centerCircleRadiusPx)
                    })
                .height(50.sdp),

            )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(180f)
                .background(
                    color = Color.White,
                    shape = GenericShape { size: Size, _: LayoutDirection ->
                        buildCustomPath(size, cornerRadiusPx, centerCircleRadiusPx)
                    })
                .height(50.sdp),

            )
    }

}

fun Path.buildCustomPath(size: Size, cornerRadius: Float, centerCircleRadius: Float) {
    val width = size.width
    val height = size.height


    val topHalfMoveLength = (width - 2 * cornerRadius - 2 * centerCircleRadius) / 2

    // 单位长度
    val smallCubeLength = centerCircleRadius / 20

    val firstCubicPoint1 = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 8 * smallCubeLength, y = 1 * smallCubeLength
    )
    val firstCubicPoint2 = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 4 * smallCubeLength, y = 16 * smallCubeLength
    )
    val firstCubicTarget = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + centerCircleRadius, y = 16 * smallCubeLength
    )
    val secondCubicPoint1 = Offset(
        x = width - firstCubicPoint2.x, y = firstCubicPoint2.y
    )
    val secondCubicPoint2 = Offset(
        x = width - firstCubicPoint1.x, y = firstCubicPoint1.y
    )
    val secondCubicTarget = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 2 * centerCircleRadius, y = 0f
    )


    moveTo(cornerRadius, 0f)
    lineTo(cornerRadius + topHalfMoveLength, 0f)

    cubicTo(
        x1 = firstCubicPoint1.x,
        y1 = firstCubicPoint1.y,
        x2 = firstCubicPoint2.x,
        y2 = firstCubicPoint2.y,
        x3 = firstCubicTarget.x,
        y3 = firstCubicTarget.y,
    )
    cubicTo(
        x1 = secondCubicPoint1.x,
        y1 = secondCubicPoint1.y,
        x2 = secondCubicPoint2.x,
        y2 = secondCubicPoint2.y,
        x3 = secondCubicTarget.x,
        y3 = secondCubicTarget.y,
    )

    lineTo(width - cornerRadius, 0f)

    arcTo(
        rect = Rect(
            topLeft = Offset(x = width - 2 * cornerRadius, y = 0f),
            bottomRight = Offset(x = width, y = 2 * cornerRadius)
        ), startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    lineTo(width, height - cornerRadius)
    arcTo(
        rect = Rect(
            topLeft = Offset(x = width - 2 * cornerRadius, y = height - 2 * cornerRadius),
            bottomRight = Offset(x = width, y = height)
        ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    lineTo(0f + cornerRadius, height)
    arcTo(
        rect = Rect(
            topLeft = Offset(x = 0f, y = height - 2 * cornerRadius),
            bottomRight = Offset(x = 2 * cornerRadius, y = height)
        ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    lineTo(0f, cornerRadius)
    arcTo(
        rect = Rect(
            topLeft = Offset.Zero, bottomRight = Offset(x = 2 * cornerRadius, y = 2 * cornerRadius)
        ), startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    close()

}


class TicketShape(private val cornerRadius: Float) : Shape {

    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawTicketPath(size = size, cornerRadius = cornerRadius)
        )
    }
}


fun drawTicketPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ), startAngleDegrees = 180.0f, sweepAngleDegrees = -90.0f, forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ), startAngleDegrees = 270.0f, sweepAngleDegrees = -90.0f, forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height)
        close()
    }
}


package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp

@Composable
fun FeatureWorkoutItems(
    value: String?,
    textSize: TextUnit,
    textColor: Color,
    backgroundColor: Color = Color.White,
    textModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(top = 5.sdp)
            .drawBehind {
                val trianglePath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(x = size.width, y = 0f)
                    lineTo(x = size.width - 10f, y = size.height)
                    lineTo(x = 0f, y = size.height)
                }

                drawPath(
                    color = backgroundColor, path = trianglePath, blendMode = BlendMode.DstOut
                )

                clipPath(trianglePath) {
                    drawRect(
                        color = backgroundColor,
                    )
                }
            }, contentAlignment = Alignment.Center
    ) {

        Text(
            text = value ?: "",
            maxLines = 1,
            modifier = textModifier.align(Alignment.CenterStart),
            style = TextStyle(
                fontSize = textSize, fontWeight = FontWeight.Bold, fontFamily = AppFont.MyCustomFont
            ),
            color = textColor,
            overflow = TextOverflow.Ellipsis,
        )

    }
}
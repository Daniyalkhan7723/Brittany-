package com.cp.brittany.dixon.utills.composecalendar.week

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import java.time.DayOfWeek
import java.time.format.TextStyle.SHORT
import java.util.Locale
import kotlin.DeprecationLevel.WARNING

@Composable
@Deprecated(
    level = WARNING,
    replaceWith = ReplaceWith(
        "DefaultDaysOfWeekHeader(daysOfWeek, modifier)",
        "com.cp.brittany.dixon.utills.composecalendar.week.DefaultDaysOfWeekHeader",
    ),
    message = "Replace with DefaultDaysOfWeekHeader, DefaultWeekHeader will be removed in future versions"
)

fun DefaultWeekHeader(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(SHORT, Locale.getDefault()),
                modifier = modifier
                    .weight(1f)
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
fun DefaultDaysOfWeekHeader(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(bottom = 10.sdp)) {
        daysOfWeek.forEach { dayOfWeek ->

            Text(
                text = dayOfWeek.getDisplayName(SHORT, Locale.getDefault()),
                modifier = modifier
                    .weight(1f)
                    .wrapContentHeight(),
                style = TextStyle(
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontFamily = AppFont.MyCustomFont
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )

        }
    }
}

internal fun <T> Array<T>.rotateRight(n: Int): List<T> = takeLast(n) + dropLast(n)

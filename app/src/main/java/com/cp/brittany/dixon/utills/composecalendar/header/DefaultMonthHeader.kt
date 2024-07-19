package com.cp.brittany.dixon.utills.composecalendar.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Utils.isCurrentMonth
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import java.time.format.TextStyle.FULL
import java.util.Locale

@Composable
fun DefaultMonthHeader(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    screenType: String
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        if (!isCurrentMonth(monthState.currentMonth.toString()) || screenType == Constants.SCHEDULE_WORKOUTS) {
            IconButton(
                modifier = Modifier
                    .testTag("Decrement")
                    .padding(start = 10.sdp),
                onClick = {
                    monthState.currentMonth = monthState.currentMonth.minusMonths(1)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    contentDescription = "Previous",
                    modifier = Modifier
                        .rotate(180f)
                        .size(21.sdp),
                )
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.Center)
        ) {

            HeadingTextComponentWithoutFullWidth(
                value = monthState.currentMonth.month
                    .getDisplayName(FULL, Locale.getDefault())
                    .lowercase()
                    .replaceFirstChar { it.titlecase() },
                textSize = 18.ssp,
                textColor = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(8.sdp))
            HeadingTextComponentWithoutFullWidth(
                value = monthState.currentMonth.year.toString(),
                textSize = 18.ssp,
                textColor = MaterialTheme.colorScheme.onBackground
            )

        }

        IconButton(
            modifier = Modifier
                .testTag("Increment")
                .padding(end = 10.sdp)
                .align(Alignment.TopEnd),
            onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_more),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = "Next",
                modifier = Modifier
                    .size(21.sdp),
            )
        }
    }
}

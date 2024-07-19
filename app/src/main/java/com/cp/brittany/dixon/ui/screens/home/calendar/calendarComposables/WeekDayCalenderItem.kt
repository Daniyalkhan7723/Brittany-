package com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.composecalendar.CalendarUiModel
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun WeekDayCalendarItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
    showSlots: Boolean,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.sdp, horizontal = 4.sdp)
            .clickable {
                onClickListener(date)
            },
        shape = RoundedCornerShape(20.sdp),
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primary
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .width(40.sdp)
                .height(70.sdp)
                .padding(vertical = 5.sdp)
        ) {

            Text(
                text = (date.day).uppercase(),
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 7.sdp),
                style = TextStyle(
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                ),
                color =
                if (date.isSelected) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.tertiary
                }
            )

            Text(
                text = date.date.dayOfMonth.toString(),
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.sdp),
                style = TextStyle(
                    fontSize = 16.ssp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.roboto_bold))
                ),
                color =
                if (date.isSelected) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.tertiary
                }
            )


            if (showSlots) {
                Box(
                    modifier =
                    if (date.isSelected) {
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 1.sdp)
                            .size(3.sdp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                    } else {
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 5.sdp)
                            .size(3.sdp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSecondary)
                    }

                )
            }


        }
    }
}

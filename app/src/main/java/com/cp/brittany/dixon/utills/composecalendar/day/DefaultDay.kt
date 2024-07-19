package com.cp.brittany.dixon.utills.composecalendar.day

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Utils.isDateBeforeCurrent
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionState
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import java.time.LocalDate

@Composable
fun <T : SelectionState> DefaultDay(
    state: DayState<T>,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit = {},
    screenType: String,
    slots: MutableList<WorkoutData> = ArrayList(),
) {
    val date = state.date
    val selectionState = state.selectionState
    val isSelected = selectionState.isDateSelected(date)
    var showDot by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .wrapContentSize()

    ) {

        Card(
            modifier = modifier
                .align(Alignment.Center)
                .size(27.sdp),
            colors = if (isSelected) CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondary, //Card background color
            ) else CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background, //Card background color
            ),
            border = if (state.isCurrentDay) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSecondary)
            } else {
                null
            },

            shape = if (state.isCurrentDay || isSelected) RoundedCornerShape(25.sdp) else RoundedCornerShape(
                0.dp
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (!(isDateBeforeCurrent(state.date.toString())) || (screenType == Constants.SCHEDULE_WORKOUTS)) {
                            onClick(date)
                            selectionState.onDateSelected(date)
                        }

                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = date.dayOfMonth.toString(), style = androidx.compose.ui.text.TextStyle(
                        fontSize = 13.ssp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = AppFont.MyCustomFont
                    ),

                    color = if (((isDateBeforeCurrent(state.date.toString())) && (screenType == Constants.WORKOUT_DETAIL_SCREEN)) || (!state.isFromCurrentMonth)) {
                        Color(
                            0xFF49494a
                        )
                    } else MaterialTheme.colorScheme.onBackground

                )
            }
        }

        slots.forEach { sloteSate ->
            if (sloteSate.date == state.date.toString()) {
                showDot = true
                return@forEach
            }
        }

        if ((showDot) && (state.isFromCurrentMonth)) {
            Box(
                modifier = if (state.isCurrentDay) {
                    Modifier
                        .align(Alignment.Center)
                        .padding(top = 37.sdp)
                        .size(3.sdp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondary)
                } else {
                    Modifier
                        .align(Alignment.Center)
                        .padding(top = 23.sdp)
                        .size(3.sdp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondary)
                }

            )

        }

    }
}

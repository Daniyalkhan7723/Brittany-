package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.AutoResizingText
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun WorkoutTopBar(
    title: String,
    backIconText: String,
    backPress: () -> Unit,
    searchClick: () -> Unit,
    endIcon: Int,
    modifier: Modifier = Modifier,
    screenType: String = "others"
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            backPress()

        })
    }

    val searchClickable by remember {
        mutableStateOf(Modifier.clickable {
            searchClick()
        })
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.sdp
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 2.sdp)
                .then(click)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .rotate(180f)
                    .size(17.sdp),

                )

            NormalTextComponentWithoutFullWidth(
                value = backIconText, textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.onBackground,
                fontStyle = FontStyle.Normal,
                modifier = Modifier.align(CenterVertically),
            )

        }


        AutoResizingText(
            text = title,
            color = MaterialTheme.colorScheme.secondary,
            targetTextSize = 20.ssp,
            modifier = Modifier
                .align(Alignment.Center)
                .width(193.sdp)
                .padding(start = 10.sdp, top = 2.sdp),
            maxLines = 1
        )

        Image(
            painter = painterResource(id = endIcon),
            contentDescription = "Search",
            modifier =

            if (screenType == Constants.SCHEDULE_WORKOUTS) {
                modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.sdp)
                    .size(22.sdp)
            } else {
                modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.sdp)
                    .then(searchClickable)
                    .size(22.sdp)
            }

        )

    }
}
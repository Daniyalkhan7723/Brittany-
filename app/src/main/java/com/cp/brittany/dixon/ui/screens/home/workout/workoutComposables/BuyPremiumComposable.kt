package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun BuyPremiumComposable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 5.sdp,
                    bottomEnd = 5.sdp
                )
            )
            .height(100.sdp),
    ) {
        Image(
            painter = painterResource(R.drawable.background_premium),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(R.drawable.logo_buy_premium),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(500.sdp)
                .fillMaxWidth()
                .align(Alignment.TopEnd)

        )

        Column(modifier = Modifier.padding(start = 5.sdp)) {
            Row(modifier = Modifier.padding(start = 5.sdp, top = 10.sdp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_buy_premium),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = stringResource(id = R.string.buy_premium),
                    modifier = Modifier.padding(start = 4.sdp),
                    style = TextStyle(
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),

                        ),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = stringResource(id = R.string.avail_to_all),
                modifier = Modifier.padding(start = 4.sdp, top = 10.sdp),
                style = TextStyle(
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),

                    ),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(id = R.string.workout_masterclasses),
                modifier = Modifier.padding(start = 4.sdp, top = 3.sdp),
                style = TextStyle(
                    fontSize = 9.ssp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),

                    ),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.padding(start = 5.sdp, top = 10.sdp),

                ) {
                Text(
                    text = stringResource(id = R.string.view_more),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 11.ssp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),

                        ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(start = 3.sdp)
                        .size(15.sdp),
                    alignment = Alignment.Center
                )
            }


        }
    }
}
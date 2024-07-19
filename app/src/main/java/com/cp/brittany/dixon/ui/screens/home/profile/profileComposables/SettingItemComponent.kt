package com.cp.brittany.dixon.ui.screens.home.profile.profileComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun SettingItemComponent(
    modifier: Modifier = Modifier,
    title: String,
    price: String = "",
    showPrice: Boolean = false,
    goNext: () -> Unit
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            goNext()
        })
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(click)
            .padding(start = 20.sdp, end = 8.sdp, bottom = 12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 15.ssp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .padding(end = 10.sdp)
        ) {
            if (showPrice) {
                Text(
                    text = price,
                    style = TextStyle(
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 5.sdp, top = 4.sdp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_more),
                modifier = Modifier.size(25.sdp),
                contentDescription = "setting"
            )
        }
    }
}
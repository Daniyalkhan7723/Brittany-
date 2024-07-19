package com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.sdp

@Composable
fun SubscriptionDetailItem(
    title: String?
) {
    Row(
        Modifier
            .padding(vertical = 10.sdp, horizontal = 15.sdp)
            .fillMaxSize()
    ) {
        // Load and display the image with AsyncImage
        Image(
            painter = painterResource(id = R.drawable.ic_bullet_points),
            contentDescription = null,
            modifier = Modifier
                .size(10.sdp)
        )

        Text(
            text = title ?: "",
            style = TextStyle(
                fontSize = 14.ssp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                color = MaterialTheme.colorScheme.onBackground,
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
//                .align(CenterVertically)
        )


    }

}
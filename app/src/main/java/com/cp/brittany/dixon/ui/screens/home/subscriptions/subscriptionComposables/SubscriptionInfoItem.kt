package com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.android.billingclient.api.Purchase
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.toDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SubscriptionInfoItem(purchase: InAppSubscriptionModel, purchaseTime: Long) {
    Box(
        modifier = Modifier
//            .then()
            .padding(horizontal = 15.sdp)
            .clip(RoundedCornerShape(13.sdp))
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            Modifier
                .padding(vertical = 10.sdp, horizontal = 15.sdp)
                .fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_sub_info),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 5.sdp)
                    .size(34.sdp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .padding(start = 10.sdp, top = 10.sdp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                var price = ""
                purchase.purchaseDetail.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()
                    ?.apply {
                        price = priceCurrencyCode + formattedPrice.replace(
                            "Rs",
                            ""
                        )
                    }

                val date = Instant.ofEpochMilli(purchaseTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                val formattedDate = formatter.format(date)

//                val expiryDate = dateTime.toLocalDate().toString()

                Text(
                    text = "$price ${purchase.purchaseDetail.title.replace("(Glampions)", "")}",
                    modifier = Modifier.padding(start = 4.sdp),
                    style = TextStyle(
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = AppFont.MyCustomFont
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )


                Spacer(Modifier.height(10.sdp))


                Text(
                    text = "Start: $formattedDate",
                    modifier = Modifier.padding(start = 4.sdp, bottom = 12.sdp), style = TextStyle(
                        fontSize = 12.ssp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = AppFont.MyCustomFont
                    ), color = MaterialTheme.colorScheme.onSecondaryContainer
                )

            }


        }
    }
}
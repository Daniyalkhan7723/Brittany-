package com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.ui.components.AutoResizingText
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.sdp

@Composable
fun SubscriptionItem(
    navigateToSubscriptionDetailScreen: (InAppSubscriptionModel) -> Unit,
    modifier: Modifier = Modifier,
    packageData: InAppSubscriptionModel
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.sdp),
        border = if (packageData.isSelected) BorderStroke(
            1.dp,
            color = MaterialTheme.colorScheme.secondary
        ) else null

    ) {
        Box(
            if (packageData.isSelected)
                Modifier
                    .background(
                        color = Color(0xFF212124),
                        shape = RoundedCornerShape(15.sdp)
                    )
                    .clip(shape = RoundedCornerShape(percent = 20))
                    .fillMaxWidth()
                    .clickable {
                        navigateToSubscriptionDetailScreen(packageData.copy(isSelected = true))
                    }
            else
                Modifier
                    .background(
                        color = Color(0xFF2C2C2E),
                        shape = RoundedCornerShape(8.sdp)
                    )
                    .clip(shape = RoundedCornerShape(percent = 20))
                    .fillMaxWidth()
                    .clickable {
                        navigateToSubscriptionDetailScreen(packageData.copy(isSelected = packageData.isSelected.not()))
                    }

        ) {
            Row(
                Modifier
                    .padding(vertical = 10.sdp, horizontal = 12.sdp)
                    .fillMaxSize()
            ) {
                // Load and display the image with AsyncImage
                Image(
                    painter =
                    if (packageData.isSelected)
                        painterResource(id = R.drawable.ic_radio_button_selected)
                    else painterResource(id = R.drawable.ic_radio_button_unselected),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.sdp)
                        .align(CenterVertically)
                )

                Column(
                    modifier = Modifier
                        .align(CenterVertically)
                )
                {
                    Box(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                    ) {

                        packageData.purchaseDetail.apply {
                            title.replace("(Glampions)", "")?.let {
                                AutoResizingText(
                                    text = it.trim(),
                                    color = Color(0xFFCED4DA),
                                    targetTextSize = 16.ssp,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .align(TopStart)
                                        .padding(start = 5.sdp)
                                )
                            }

                            subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()
                                ?.apply {
                                    AutoResizingText(
                                        text = "${priceCurrencyCode}${
                                            formattedPrice.replace(
                                                "Rs",
                                                ""
                                            )
                                        } ${
                                            when {
                                                billingPeriod.contains("W") -> "/w"

                                                billingPeriod.contains("M") -> "/m"

                                                else -> "/y"
                                            }
                                        }",
                                        color = Color(0xFFCED4DA),
                                        targetTextSize = 16.ssp,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .align(TopEnd)
                                    )
                                }

                        }


                    }


                    Text(
                        text = packageData.purchaseDetail.description,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.sdp),
                        style = TextStyle(
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontFamily = AppFont.MyCustomFont,
                        ), color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }


            }
        }
    }

}
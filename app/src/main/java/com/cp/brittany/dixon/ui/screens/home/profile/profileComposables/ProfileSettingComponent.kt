package com.cp.brittany.dixon.ui.screens.home.profile.profileComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.billingclient.api.Purchase
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.DetailRoute
import com.cp.brittany.dixon.ui.components.AutoResizingText
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun ProfileSettingComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    purchaseDetailsList: MutableList<InAppSubscriptionModel>

) {
    var priceValue: String? = null
    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(MaterialTheme.colorScheme.background)

    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SettingItemComponent(title = stringResource(R.string.change_password),
                modifier = Modifier.padding(top = 15.sdp),
                goNext = {
                    navController.navigate(DetailRoute.ResetPassword.route)
                })
            Spacer(
                modifier = Modifier
                    .height((1).dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            if (purchaseDetailsList.isNotEmpty()) {
                purchaseDetailsList.first().purchaseDetail.subscriptionOfferDetails?.first()?.pricingPhases?.pricingPhaseList?.first()
                    ?.apply {
                        priceValue = "${priceCurrencyCode}${
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
                        }"
                    }
            }



            SettingItemComponent(title = stringResource(R.string.subscription),
                modifier = Modifier.padding(top = 15.sdp),
                price = priceValue ?: "",
                showPrice = purchaseDetailsList.isNotEmpty(),
                goNext = {
                    navController.navigate(DetailRoute.SubscriptionInfo.route)
                })



            Spacer(
                modifier = Modifier
                    .height((1).dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(modifier = Modifier.height(12.sdp))

            SettingItemComponent(title = stringResource(R.string.invite_friends), goNext = {

            })
            Spacer(
                modifier = Modifier
                    .height((1).dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(modifier = Modifier.height(12.sdp))


            SettingItemComponent(title = stringResource(R.string.faqs_feedback), goNext = {

            })

        }
    }
}
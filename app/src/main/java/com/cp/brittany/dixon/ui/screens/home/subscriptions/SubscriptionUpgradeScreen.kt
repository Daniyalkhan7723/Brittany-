package com.cp.brittany.dixon.ui.screens.home.subscriptions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.BorderButtonRound
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables.SubscriptionInfoItem
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.subscription.SubscriptionUpgradeViewModel
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SubscriptionUpgrade(
    subscriptionUpgradeViewModel: SubscriptionUpgradeViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
    navigateToSubscriptionProductsScreen: () -> Unit
) {

    val paddingValues = PaddingValues(
        horizontal = 20.sdp,
        vertical = 12.sdp
    )

    val activity = LocalContext.current as Activity

    val click by remember {
        mutableStateOf(Modifier.clickable {

        })
    }

    val cancelPlan = remember {
        {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/account/subscriptions")
            )
            activity.startActivity(browserIntent)

        }
    }

    val backPress = remember {
        {
            onBackPress()

        }
    }

    LaunchedEffect(key1 = Unit) {
        subscriptionUpgradeViewModel.billingSetup()
    }

    BrittanyDixonTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 20.sdp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart,

            ) {


            if (subscriptionUpgradeViewModel.subscriptionPurchasedList.isNotEmpty()) {
                Column(
                    modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(25.sdp))
                    ProfileTopBar(
                        title = stringResource(R.string.subscription_info), backPress = backPress
                    )
                    Spacer(modifier = Modifier.height(15.sdp))

//                Column(
//                    modifier = Modifier
//                        .padding(start = 15.sdp, top = 10.sdp)
//                        .fillMaxWidth(),
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    HeadingTextComponentWithoutFullWidth(
//                        value = "Manage Plan",
//                        modifier = Modifier.padding(start = 4.sdp),
//                        textColor = MaterialTheme.colorScheme.onBackground,
//                        textSize = 14.ssp
//                    )
//
//                    Spacer(Modifier.height(7.sdp))
//                    HeadingTextComponentWithoutFullWidth(
//                        value = "Monthly - Next Payment on 17 June 2023",
//                        modifier = Modifier.padding(start = 4.sdp, bottom = 15.sdp),
//                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                        textSize = 12.ssp
//                    )
//                }

//                Spacer(modifier = Modifier.height(8.sdp))


                    LazyColumn {
                        items(
                            count = subscriptionUpgradeViewModel.subscriptionProductList.size,
                            key = {
                                subscriptionUpgradeViewModel.subscriptionProductList[it].purchaseDetail.productId
                                    ?: ""
                            },
                            itemContent = { index ->
                                SubscriptionInfoItem(
                                    subscriptionUpgradeViewModel.subscriptionProductList[index],
                                    subscriptionUpgradeViewModel.subscriptionPurchasedList[index].purchaseTime
                                )
                            })

                    }
                }

                Column(
                    modifier = Modifier
                        .padding(
                            start = 20.sdp,
                            end = 20.sdp,
                            bottom = 20.sdp
                        )
                        .align(Alignment.BottomEnd)
                ) {
                    ButtonComponent(
                        value = stringResource(R.string.upgrade_plan),
                        onButtonClicked = navigateToSubscriptionProductsScreen,
                        buttonColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.background,
                    )
                    Spacer(modifier = Modifier.height(15.sdp))


                    BorderButtonRound(
                        value = stringResource(R.string.cancel_plan),
                        onButtonClicked = cancelPlan,
                        paddingValues = paddingValues,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shapeRadius = 48,
                        borderRadius = 50
                    )
                }

            } else {
                NoDataFound(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }

    }
}
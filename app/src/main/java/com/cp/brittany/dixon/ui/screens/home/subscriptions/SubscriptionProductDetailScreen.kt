package com.cp.brittany.dixon.ui.screens.home.subscriptions

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.subscriptionInfo.SubscriptionPlanUIEvent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables.SubscriptionDetailItem
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.subscription.SubscriptionPlanDetailViewModel
import com.cp.brittany.dixon.ui.viewModel.subscription.SubscriptionPlanViewModel
import com.cp.brittany.dixon.utills.ChooseSubscription.Companion.billingClient
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.toDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.IconButton as IconButton1

@Composable
fun SubscriptionProductDetailScreen(
    onBackPress: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    subscriptionPlanDetailViewModel: SubscriptionPlanDetailViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    packageId: Int
) {
    subscriptionPlanDetailViewModel.apply {
        val coroutineScope = rememberCoroutineScope()
        val activity = LocalContext.current as Activity
        val context = LocalContext.current

        val handleSubscriptionResponse by subscriptionPlanDetailViewModel.handleSubscriptionResponse.collectAsStateWithLifecycle()
        val handleSubscriptionSuccess by subscriptionSuccess.collectAsState()
        LaunchedEffect(key1 = handleSubscriptionSuccess?.boolean == true) {
            if (handleSubscriptionSuccess?.boolean == true) {
                handleSubscriptionSuccess?.boolean = false
                val jsonObject =
                    handleSubscriptionSuccess?.purchase?.originalJson?.let { JSONObject(it) }
                val productId = jsonObject?.get("productId")

                val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT'xxx uuuu")
                val dateTime = LocalDateTime.parse(
                    handleSubscriptionSuccess?.purchase?.purchaseTime?.toDate(productId.toString())
                        .toString(), formatter
                )
                val expiryDate = dateTime.toLocalDate().toString()

                subscriptionPlanDetailViewModel.onEvent(
                    SubscriptionPlanUIEvent.SubscriptionSuccess(
                        productId = productId.toString(),
                        expiryData = expiryDate,
                        token = handleSubscriptionSuccess?.purchase?.purchaseToken ?: "",
                        inAppResponse = JSONObject(handleSubscriptionSuccess?.purchase?.originalJson).toString(),
                        packageId = packageId.toString()
                    )
                )
            }

        }

        if (subscriptionApiResponse){
            when (handleSubscriptionResponse) {
                is NetworkResult.Success<*> -> {
                    subscriptionApiResponse = false
                    loaderState = false
                    if (handleSubscriptionResponse.data?.status == true) {
                        coroutineScope.launch {
                            if (billingClient != null) {
                                billingClient?.endConnection()
                            }
                            onBackPress()
                            resetState()
                        }
                        showToast(
                            title = handleSubscriptionResponse.data?.message ?: "", isSuccess = true
                        )
                    } else {
                        showToast(
                            title = handleSubscriptionResponse.data?.message ?: "", isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    resetResponse()
                    subscriptionApiResponse = false
                    loaderState = false
                    val message: String = try {
                        val jObjError = JSONObject(handleSubscriptionResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        handleSubscriptionResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    loaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    loaderState = false
                    subscriptionApiResponse = false
                    resetResponse()
                    showToast(
                        title = handleSubscriptionResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }



        Box {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(400.sdp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.dummy_image3),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.blur_view_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                ProfileTopBar(
                    title = stringResource(R.string.subscription_plan),
                    backPress = onBackPress,
                    showBackButtonText = false,
                    modifier = Modifier.padding(top = 40.sdp)
                )
            }

            Card(
                shape = RoundedCornerShape(15.sdp),
                elevation = CardDefaults.cardElevation(5.dp),
                onClick = {

                },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C2C2E)
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .align(BottomCenter)
                    .padding(15.sdp)
            ) {
                Box(modifier = Modifier) {
                    val iconImage = painterResource(id = R.drawable.ic_close)
                    IconButton1(
                        onClick = {

                        }, modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Image(
                            painter = iconImage,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.sdp)
                                .size(24.sdp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(top = 16.sdp)
                    ) {
                        Text(
                            text = sharedViewModel.packageDetailResponse.value?.title?.replace(
                                "(Glampions)", ""
                            )?.trim() ?: "",
                            style = TextStyle(
                                fontSize = 15.ssp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                color = MaterialTheme.colorScheme.onSecondary,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Row(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(top = 5.sdp),
                        ) {


                            Text(
                                text = sharedViewModel.packageDetailResponse.value?.subscriptionOfferDetails?.get(
                                    0
                                )?.pricingPhases?.pricingPhaseList?.get(
                                    0
                                )?.priceCurrencyCode ?: "", style = TextStyle(
                                    fontSize = 21.ssp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    color = MaterialTheme.colorScheme.onBackground,
                                ), textAlign = TextAlign.Center
                            )

                            Row(modifier = Modifier.padding(top = 5.sdp)) {
                                sharedViewModel.packageDetailResponse.value?.subscriptionOfferDetails?.get(
                                    0
                                )?.pricingPhases?.pricingPhaseList?.get(
                                    0
                                )?.formattedPrice?.replace("Rs", "")?.let {
                                    Text(
                                        text = it,
                                        style = TextStyle(
                                            fontSize = 35.ssp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                            color = MaterialTheme.colorScheme.onBackground,
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.align(Bottom)
                                    )
                                }



                                Text(
                                    text = when {
                                        sharedViewModel.packageDetailResponse.value?.subscriptionOfferDetails?.get(
                                            0
                                        )?.pricingPhases?.pricingPhaseList?.get(
                                            0
                                        )?.billingPeriod?.contains("W") == true -> "/w"

                                        sharedViewModel.packageDetailResponse.value?.subscriptionOfferDetails?.get(
                                            0
                                        )?.pricingPhases?.pricingPhaseList?.get(
                                            0
                                        )?.billingPeriod?.contains("M") == true -> "/m"

                                        else -> "/y"
                                    },
                                    style = TextStyle(
                                        fontSize = 19.ssp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Bottom)
                                        .padding(bottom = 5.sdp)
                                )
                            }

                        }

                        LazyColumn {
                            items(count = detailList.size) { countValue ->
                                detailList[countValue]
                                SubscriptionDetailItem(detailList[countValue])
                            }
                        }

                        ButtonComponent(
                            value = stringResource(R.string.subscribe),
                            onButtonClicked = {
                                if (subscriptionPurchasedList.isEmpty()) {
                                    sharedViewModel.packageDetailResponse.value?.let {
                                        launchPurchaseFlow(
                                            it, activity
                                        )
                                    }
                                } else {
                                    sharedViewModel.packageDetailResponse.value?.let {
                                        upgradePlan(
                                            it, activity
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(CenterHorizontally)
                                .padding(
                                    bottom = 20.sdp, start = 15.sdp, end = 15.sdp, top = 10.sdp
                                ),
                            buttonColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.background,
                        )

                    }

                }

            }


        }
    }
}
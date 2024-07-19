package com.cp.brittany.dixon.ui.screens.home.subscriptions

import android.app.Activity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
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
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.model.subscription.PackageDetailModel
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.HeadingTextComponent
import com.cp.brittany.dixon.ui.components.findActivity
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables.SubscriptionDetailItem
import com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables.allPackages
import com.cp.brittany.dixon.ui.viewModel.subscription.SubscriptionPlanViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.toDate
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SubscriptionProductsScreen(
    onBackPress: () -> Unit,
    subscriptionPlanViewModel: SubscriptionPlanViewModel = hiltViewModel(),
) {
    subscriptionPlanViewModel.apply {
        val activity = LocalContext.current as Activity
        val appCompactActivity = LocalContext.current.findActivity()
        val context = LocalContext.current
        val getPackagesData by getPackages.collectAsStateWithLifecycle()
        val handleSubscriptionResponse by handleSubscriptionResponse.collectAsStateWithLifecycle()
        val alreadyPurchaseResponse by alreadyPurchase.collectAsStateWithLifecycle()
        val coroutineScope = rememberCoroutineScope()

        val onBackPressedCallback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (subscriptionPlanViewModel.showHideDetail) {
                        subscriptionPlanViewModel.showHideDetail = false
                        subscriptionPlanViewModel.showHidePackageList = true
                    } else {
                        coroutineScope.launch {
                            if (billingClient != null) {
                                billingClient?.endConnection()
                            }
                            onBackPress()
                        }
                    }

                }
            }
        }
        val onBackPressedDispatcher = appCompactActivity?.onBackPressedDispatcher
        DisposableEffect(onBackPressedDispatcher) {
            onBackPressedDispatcher?.addCallback(onBackPressedCallback)
            onDispose { onBackPressedCallback.remove() }
        }

        val backPressClick = remember {
            {
                if (subscriptionPlanViewModel.showHideDetail) {
                    subscriptionPlanViewModel.showHideDetail = false
                    subscriptionPlanViewModel.showHidePackageList = true
                } else {
                    if (billingClient != null) {
                        billingClient?.endConnection()
                    }
                    onBackPress()
                }

            }
        }

        val onClickCross = remember {
            {
                showHideDetail = false
                showHidePackageList = true
            }
        }


        subscriptionPlanViewModel.apply {
            val handleSubscriptionSuccess by subscriptionSuccess.collectAsState()

            if (alreadyPurchaseResponse) {
                resetAlreadyPurchaseResponse()
                showToast(
                    title = stringResource(R.string.already_subscribed), isSuccess = false
                )
            }

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

                    var packageId = 0
                    packageDataFromServer.forEachIndexed { index, packageData ->
                        if (packageData.inapp_android_package == productId) {
                            packageId = packageData.id ?: 0
                            return@forEachIndexed
                        }
                    }
                    subscriptionPlanViewModel.onEvent(
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


            var onNavigateToPackageDetails = remember<(InAppSubscriptionModel) -> Unit> {
                { data ->
                    var packageId = 0
                    packageDataFromServer.forEachIndexed { index, packageData ->
                        if (packageData.inapp_android_package == data.purchaseDetail.productId) {
                            packageId = packageData.id ?: 0
                            return@forEachIndexed
                        }
                    }

                    showHidePackageList = false
                    showHideDetail = true
                    packageDetailModel = PackageDetailModel(packageId, data.purchaseDetail)
                    subscriptionPlanViewModel.onItemSelected(data)
                }
            }

            when (getPackagesData) {
                is NetworkResult.Success<*> -> {
                    loaderState = false
                    if (getPackagesData.data?.status == true) {
                        getPackagesData.data?.data?.let { data ->
                            packageDataFromServer.clear()
                            data.let { list -> packageDataFromServer.addAll(list) }
                        }
                    } else {
                        showToast(
                            title = getPackagesData.data?.message ?: "", isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    resetResponse()
                    loaderState = false
                    val message: String = try {
                        val jObjError = JSONObject(getPackagesData.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        getPackagesData.message ?: context.resources.getString(
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
                    resetResponse()
                    showToast(
                        title = getPackagesData.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }

            if (subscriptionApiResponse) {
                when (handleSubscriptionResponse) {
                    is NetworkResult.Success<*> -> {
                        loaderState = false
                        subscriptionApiResponse = false
                        if (handleSubscriptionResponse.data?.status == true) {
                            coroutineScope.launch {
                                if (billingClient != null) {
                                    billingClient?.endConnection()
                                }
                                onBackPress()
                                resetState()
                            }

                            showToast(
                                title = handleSubscriptionResponse.data?.message ?: "",
                                isSuccess = true
                            )
                        } else {
                            showToast(
                                title = handleSubscriptionResponse.data?.message ?: "",
                                isSuccess = false
                            )
                        }
                        resetResponse()
                    }

                    is NetworkResult.Error<*> -> {
                        subscriptionApiResponse = false
                        resetResponse()
                        loaderState = false
                        val message: String = try {
                            val jObjError =
                                JSONObject(handleSubscriptionResponse.message.toString())
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
                        subscriptionApiResponse = false
                        loaderState = false
                        resetResponse()
                        showToast(
                            title = handleSubscriptionResponse.message ?: "", isSuccess = false
                        )

                    }

                    is NetworkResult.NoCallYet<*> -> {

                    }
                }
            }


            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    item {
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



                            if (showHidePackageList) {
                                HeadingTextComponent(
                                    value = stringResource(R.string.subscription_membership),
                                    textSize = 20.ssp,
                                    modifier = Modifier
                                        .padding(
                                            start = 15.sdp, end = 15.sdp, bottom = 30.sdp
                                        )
                                        .align(
                                            BottomCenter
                                        )
                                )
                            }

                        }
                    }


                    if (showHidePackageList) {
                        allPackages(
                            allPackagesList = subscriptionProductList,
                            navigateToPackageDetailScreen = onNavigateToPackageDetails,
                            modifier = Modifier
                        )

                        item {
                            val onClickSubscribe = remember {
                                {
                                    val selected =
                                        subscriptionProductList.filter { item -> item.isSelected }
                                    if (selected.isNotEmpty()) {
                                        if (subscriptionPurchasedList.isEmpty()) {
                                            launchPurchaseFlow(
                                                selected.first().purchaseDetail, activity
                                            )
                                        } else {
                                            upgradePlan(selected.first().purchaseDetail, activity)
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please select one subscription plan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            ButtonComponent(
                                value = stringResource(R.string.subscribe),
                                onButtonClicked = onClickSubscribe,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomEnd)
                                    .heightIn(45.sdp)
                                    .padding(
                                        bottom = 20.sdp, start = 15.sdp, end = 15.sdp, top = 10.sdp
                                    ),
                                buttonColor = MaterialTheme.colorScheme.secondary,
                                textColor = MaterialTheme.colorScheme.background,
                            )
                        }
                    }

                }


                if (showHideDetail) {
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
                            IconButton(
                                onClick = onClickCross, modifier = Modifier.align(Alignment.TopEnd)
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
                                    text = packageDetailModel.purchaseDetail?.title?.replace(
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
                                        text = packageDetailModel.purchaseDetail?.subscriptionOfferDetails?.get(
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
                                        packageDetailModel.purchaseDetail?.subscriptionOfferDetails?.get(
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
                                                packageDetailModel.purchaseDetail?.subscriptionOfferDetails?.get(
                                                    0
                                                )?.pricingPhases?.pricingPhaseList?.get(
                                                    0
                                                )?.billingPeriod?.contains("W") == true -> "/w"

                                                packageDetailModel.purchaseDetail?.subscriptionOfferDetails?.get(
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
                                            packageDetailModel.purchaseDetail?.let {
                                                launchPurchaseFlow(
                                                    it, activity
                                                )
                                            }
                                        } else {
                                            packageDetailModel.purchaseDetail?.let {
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
                                            bottom = 20.sdp,
                                            start = 15.sdp,
                                            end = 15.sdp,
                                            top = 10.sdp
                                        ),
                                    buttonColor = MaterialTheme.colorScheme.secondary,
                                    textColor = MaterialTheme.colorScheme.background,
                                )

                            }

                        }

                    }

                }

                ProfileTopBar(
                    title = stringResource(R.string.subscription_plan),
                    backPress = backPressClick,
                    showBackButtonText = false,
                    modifier = Modifier.padding(top = 40.sdp)
                )


                if (loaderState) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.sdp)
                            .align(Center),
                        strokeWidth = 3.sdp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }


        }
    }
}
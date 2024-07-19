package com.cp.brittany.dixon.ui.viewModel.subscription

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.subscriptionInfo.SubscriptionPlanUIEvent
import com.cp.brittany.dixon.data.subscriptionInfo.SubscriptionPlanUIState
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.subscription.GetPackagesResponse
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.model.subscription.PackageData
import com.cp.brittany.dixon.model.subscription.PackageDetailModel
import com.cp.brittany.dixon.model.subscription.SubscriptionSuccessModel
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.common.collect.ImmutableList
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SubscriptionPlanViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
//    val chooseSubscription: ChooseSubscription,
) : ViewModel() {
    private val _getPackages: MutableStateFlow<NetworkResult<GetPackagesResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getPackages: StateFlow<NetworkResult<GetPackagesResponse>> = _getPackages

    private val _handleSubscriptionResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val handleSubscriptionResponse: StateFlow<NetworkResult<LoginResponse>> =
        _handleSubscriptionResponse

    private var _subscriptionSuccess: MutableStateFlow<SubscriptionSuccessModel?> =
        MutableStateFlow(null)
    var subscriptionSuccess: StateFlow<SubscriptionSuccessModel?> = _subscriptionSuccess

    private val _alreadyPurchase: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val alreadyPurchase: MutableStateFlow<Boolean> =
        _alreadyPurchase


    var subscriptionProductList = SnapshotStateList<InAppSubscriptionModel>()
    var subscriptionPurchasedList = SnapshotStateList<Purchase>()
    var packageDataFromServer = SnapshotStateList<PackageData>()
    var loaderState by mutableStateOf(false)

    var showHideDetail by mutableStateOf(false)
    var showHidePackageList by mutableStateOf(true)
    var packageDetailModel by mutableStateOf(PackageDetailModel())


    var detailList = mutableListOf<String>()
    var subscriptionApiResponse by mutableStateOf(false)

    var billingClient: BillingClient? = null
    var subscriptionUIState = mutableStateOf(SubscriptionPlanUIState())
    private val TAG = SubscriptionPlanViewModel::class.simpleName

    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // User canceled the purchase
        } else {
            // Handle other error cases
        }
    }

    init {
        detailList.apply {
            add("An The full context of your organization’s message history at your fingertips.")
            add("An Timely info and actions in one place with unlimited integrations pair")
            add("Face-to-face communication")
            add("Secure collaboration with outside organizans or guests from within Slack")
        }
        getAllPackages()
        if (billingClient == null) {
            billingClient =
                BillingClient.newBuilder(applicationContext).setListener(purchaseUpdateListener)
                    .enablePendingPurchases().build()

        }
        billingSetup()
    }


    fun onEvent(event: SubscriptionPlanUIEvent) {
        when (event) {
            is SubscriptionPlanUIEvent.SubscriptionSuccess -> {
                event.apply {
                    subscriptionUIState.value = subscriptionUIState.value.copy(
                        productId = productId,
                        expiryData = expiryData,
                        token = token,
                        packageId = packageId,
                        inAppResponse = inAppResponse
                    )
                }
                subscriptionApiResponse = true
                purchase()
            }
        }
    }


    fun billingSetup() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    viewModelScope.launch {
                        getQueryPurchasesAsync()
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                // Handle billing service disconnection
            }
        })
    }

    private suspend fun getQueryPurchasesAsync() {
        val params =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS)

        // uses queryPurchasesAsync Kotlin extension function
        val purchasesResult = withContext(Dispatchers.IO) {
            billingClient?.queryPurchasesAsync(params.build())
        }

        // check purchasesResult.billingResult
        // process returned purchasesResult.purchasesList, e.g. display the plans user owns
        when (purchasesResult?.billingResult?.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchasesResult.purchasesList.isNotEmpty()) {
                    purchasesResult.purchasesList.let { subscriptionPurchasedList.addAll(it) }
                }
                showProducts()
            }

            else -> {
                Log.e(TAG, purchasesResult?.billingResult?.debugMessage ?: "")
            }
        }

    }

    private fun showProducts() {
        val productList = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder().setProductId("bd_one_month_sub_new")
                .setProductType(BillingClient.ProductType.SUBS).build(),
            QueryProductDetailsParams.Product.newBuilder().setProductId("bd_half_yearly_sub")
                .setProductType(BillingClient.ProductType.SUBS).build(),
            QueryProductDetailsParams.Product.newBuilder().setProductId("bd_yearly_sub_new")
                .setProductType(BillingClient.ProductType.SUBS).build()
        )
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        billingClient?.queryProductDetailsAsync(
            queryProductDetailsParams
        ) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                if (productDetailsList.isNullOrEmpty()) {
                    Log.e(
                        TAG,
                        "onProductDetailsResponse: " + "Found null or empty ProductDetails. " + "Check to see if the Products you requested are correctly " + "published in the Google Play Console."
                    )
                } else {
                    if (subscriptionProductList.isEmpty()) {
                        productDetailsList.forEach { item ->
                            subscriptionProductList.add(
                                InAppSubscriptionModel(
                                    purchaseDetail = item
                                )
                            )
                        }
                    }


                }
            }
        }
    }


    fun launchPurchaseFlow(productDetails: ProductDetails, activity: Activity) {
        assert(productDetails.subscriptionOfferDetails != null)
        val productDetailsParamsList = ImmutableList.of(
            BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken).build()
        )
        val billingFlowParams =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                .build()
        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }

    fun upgradePlan(product: ProductDetails, activity: Activity) {
        if (subscriptionPurchasedList.isEmpty()) {
            return
        }
        val activePurchase = subscriptionPurchasedList.first()

        if (JSONObject(activePurchase.originalJson).get("productId")
                .toString() == product.productId
        ) {
            _alreadyPurchase.value = true
//            requireActivity().showToast(resources.getString(R.string.already_subscribed), false)
            return
        }

        val billingParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(product)
                    .setOfferToken(product.subscriptionOfferDetails?.first()?.offerToken ?: "")
                    .build()
            )
        ).setSubscriptionUpdateParams(
            BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                .setOldPurchaseToken(activePurchase.purchaseToken).setSubscriptionReplacementMode(
                    BillingFlowParams.SubscriptionUpdateParams.ReplacementMode.CHARGE_FULL_PRICE
                ).build()
        ).build()

        billingClient?.launchBillingFlow(
            activity, billingParams
        )
    }

    private fun handlePurchase(purchase: Purchase) = viewModelScope.launch(Dispatchers.IO) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()

                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _subscriptionSuccess.value = SubscriptionSuccessModel(true, purchase)

                    }
                }
            }
        }
    }

    private fun getAllPackages() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _getPackages.value = NetworkResult.Loading()
            repository.getAllPackages().collect { response ->
                response.apply {
                    _getPackages.value = response
                }
            }
        } else {
            _getPackages.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    private fun purchase() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _handleSubscriptionResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.apply {
                addProperty("package_id", subscriptionUIState.value.packageId)
                addProperty("in_app_id", subscriptionUIState.value.token)
                addProperty("expire_date", subscriptionUIState.value.expiryData)
                addProperty("inapp_response", subscriptionUIState.value.inAppResponse)
                addProperty("in_app_type", "google")
            }

            repository.purchase(jsonObject).collect { response ->
                response.apply {
                    _handleSubscriptionResponse.value = response
                    if (response.data?.status == true) {
                        response.data.data.let {
                            preference.saveUser(it)
                        }
                    }
                }
            }
        } else {
            _handleSubscriptionResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }


    fun onItemSelected(data: InAppSubscriptionModel) {
        val iterator = subscriptionProductList.listIterator()
        while (iterator.hasNext()) {
            val listItem = iterator.next()
            iterator.set(
                if (listItem.purchaseDetail.productId == data.purchaseDetail.productId) {
                    data
                } else {
                    listItem.copy(isSelected = false)
                }
            )
        }

    }

    fun resetResponse() {
        _getPackages.value = NetworkResult.NoCallYet()
        _handleSubscriptionResponse.value = NetworkResult.NoCallYet()

    }

    fun resetState() {
        _subscriptionSuccess.value = null
    }

    fun resetAlreadyPurchaseResponse() {
        alreadyPurchase.value = false
    }


}



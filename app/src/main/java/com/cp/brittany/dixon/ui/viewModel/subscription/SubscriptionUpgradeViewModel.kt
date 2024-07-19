package com.cp.brittany.dixon.ui.viewModel.subscription

import android.app.Application
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import com.cp.brittany.dixon.data.subscriptionInfo.SubscriptionInfoUIEvent
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SubscriptionUpgradeViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
) : ViewModel() {
    private val _subscriptionInfoResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val subscriptionInfoResponse: StateFlow<NetworkResult<LoginResponse>> =
        _subscriptionInfoResponse
    private val TAG = SubscriptionUpgradeViewModel::class.simpleName
    var subscriptionPurchasedList = SnapshotStateList<Purchase>()
    var subscriptionProductList = SnapshotStateList<InAppSubscriptionModel>()
    var billingClient: BillingClient? = null

    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {

        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // User canceled the purchase
        } else {
            // Handle other error cases
        }
    }

    init {
        if (billingClient == null) {
            billingClient =
                BillingClient.newBuilder(applicationContext).setListener(purchaseUpdateListener)
                    .enablePendingPurchases().build()

        }

    }

    fun billingSetup() {
        if (billingClient?.isReady == true) {
            viewModelScope.launch {
                getQueryPurchasesAsync()
            }
        } else {
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
                subscriptionPurchasedList.clear()
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
        if (subscriptionPurchasedList.isEmpty()) {
            return
        }
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        subscriptionPurchasedList.forEach {
            val jsonObject = JSONObject(it.originalJson)
            val productId = jsonObject.get("productId")
            productList.add(
                QueryProductDetailsParams.Product.newBuilder().setProductId(productId.toString())
                    .setProductType(BillingClient.ProductType.SUBS).build()
            )
        }
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
                    subscriptionProductList.clear()
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


    fun onEvent(event: SubscriptionInfoUIEvent) {
        when (event) {
            is SubscriptionInfoUIEvent.UpgradePlanButtonClicked -> {

            }

            is SubscriptionInfoUIEvent.CancelPlanButtonClicked -> {

            }
        }
    }

    fun resetResponse() {
        _subscriptionInfoResponse.value = NetworkResult.NoCallYet()
    }


}



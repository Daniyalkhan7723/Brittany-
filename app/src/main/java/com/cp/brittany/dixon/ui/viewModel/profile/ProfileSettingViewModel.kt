package com.cp.brittany.dixon.ui.viewModel.profile

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.profileSettings.ProfileSettingEvent
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.model.auth.User
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.ui.viewModel.subscription.SubscriptionUpgradeViewModel
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.getMyDeviceId
import com.cp.brittany.dixon.utills.isNetworkAvailable
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
class ProfileSettingViewModel @Inject constructor
    (
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,

    ) : ViewModel() {
    private val _logoutResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val logoutResponse: StateFlow<NetworkResult<LoginResponse>> = _logoutResponse


    private val _getProfile: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getProfile: StateFlow<NetworkResult<LoginResponse>> = _getProfile
    var billingClient: BillingClient? = null
    var subscriptionPurchasedList = SnapshotStateList<Purchase>()
    var subscriptionProductList = SnapshotStateList<InAppSubscriptionModel>()

    private val TAG = ProfileSettingViewModel::class.simpleName


    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {

        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // User canceled the purchase
        } else {
            // Handle other error cases
        }
    }

    init {
        getProfile()
        if (billingClient == null) {
            billingClient =
                BillingClient.newBuilder(applicationContext).setListener(purchaseUpdateListener)
                    .enablePendingPurchases().build()

        }

        billingSetup()

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


    private fun logout() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _logoutResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("device_id", applicationContext.getMyDeviceId())

            repository.logout(jsonObject).collect { response ->
                response.apply {
                    _logoutResponse.value = response
                }
            }
        } else {
            _logoutResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    private fun getProfile() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _getProfile.value = NetworkResult.Loading()
            repository.getProfile().collect { response ->
                response.apply {
                    _getProfile.value = response
                }
            }
        } else {
            _getProfile.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    fun onEvent(event: ProfileSettingEvent) {
        when (event) {
            is ProfileSettingEvent.LogoutButton -> {
                logout()
            }
        }
    }

    fun resetResponse() {
        _logoutResponse.value = NetworkResult.NoCallYet()
        _getProfile.value = NetworkResult.NoCallYet()
    }


}



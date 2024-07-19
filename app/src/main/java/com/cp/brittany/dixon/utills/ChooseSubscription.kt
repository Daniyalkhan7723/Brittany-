package com.cp.brittany.dixon.utills

import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.cp.brittany.dixon.model.subscription.SubscriptionSuccessModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class ChooseSubscription @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _subscriptions = MutableStateFlow<List<String>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    private val _subscriptionSuccess: MutableStateFlow<SubscriptionSuccessModel?> =
        MutableStateFlow(null)
    val subscriptionSuccess = _subscriptionSuccess.asStateFlow()


    protected val scope = CoroutineScope(Dispatchers.Main)
    private val TAG = ChooseSubscription::class.simpleName


    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (result.responseCode == BillingResponseCode.USER_CANCELED) {
            // User canceled the purchase
        } else {
            // Handle other error cases
        }
    }


    companion object {
        var billingClient: BillingClient? = null
    }


    init {
        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(context).setListener(purchaseUpdateListener)
                .enablePendingPurchases().build()

        }


    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()

                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        scope.launch {
                            Log.d("xssxdxdxsxdwdxd","Suscess")
                            _subscriptionSuccess.value = SubscriptionSuccessModel(true, purchase)
                        }
                    }
                }
            }
        }
    }


}



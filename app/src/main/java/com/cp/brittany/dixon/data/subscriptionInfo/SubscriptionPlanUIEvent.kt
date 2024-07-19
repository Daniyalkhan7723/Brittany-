package com.cp.brittany.dixon.data.subscriptionInfo

sealed class SubscriptionPlanUIEvent {
    data class SubscriptionSuccess(
        val productId: String,
        val expiryData: String,
        val token: String,
        val inAppResponse: String,
        val packageId: String
    ) :
        SubscriptionPlanUIEvent()
}

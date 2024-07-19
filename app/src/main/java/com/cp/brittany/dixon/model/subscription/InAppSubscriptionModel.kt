package com.cp.brittany.dixon.model.subscription

import com.android.billingclient.api.ProductDetails

data class InAppSubscriptionModel(
    val isSelected: Boolean = false,
    val purchaseDetail: ProductDetails
)

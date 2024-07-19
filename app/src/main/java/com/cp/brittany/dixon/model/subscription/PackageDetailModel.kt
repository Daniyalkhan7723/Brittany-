package com.cp.brittany.dixon.model.subscription

import com.android.billingclient.api.ProductDetails

data class PackageDetailModel(
    val packageId: Int?=null,
    val purchaseDetail: ProductDetails?=null
)

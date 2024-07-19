package com.cp.brittany.dixon.model.subscription

import com.android.billingclient.api.Purchase

data class SubscriptionSuccessModel(var boolean: Boolean?=null, val purchase: Purchase?=null)

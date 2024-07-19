package com.cp.brittany.dixon.data.subscriptionInfo


sealed class SubscriptionInfoUIEvent {

    object UpgradePlanButtonClicked : SubscriptionInfoUIEvent()
    object CancelPlanButtonClicked : SubscriptionInfoUIEvent()


}

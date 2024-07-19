package com.cp.brittany.dixon.ui.screens.home.subscriptions.subscriptionComposables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.subscription.InAppSubscriptionModel
import com.cp.brittany.dixon.utills.sdp

fun LazyListScope.allPackages(
    allPackagesList: List<InAppSubscriptionModel>,
    navigateToPackageDetailScreen: (InAppSubscriptionModel) -> Unit,
    modifier: Modifier = Modifier
) {

    items(count = allPackagesList.size, key = {
        allPackagesList[it].purchaseDetail.productId
    }, itemContent = { index ->

        SubscriptionItem(
            modifier = modifier.padding(horizontal = 10.sdp, vertical = 6.sdp),
            navigateToSubscriptionDetailScreen = navigateToPackageDetailScreen,
            packageData = allPackagesList[index]
        )
    })
}
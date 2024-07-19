package com.cp.brittany.dixon.ui.screens.home.insight.insightComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun HeaderWithSearch(
    navigateToSearchAllInsights: () -> Unit,
    modifier: Modifier = Modifier,
    title: String

) {

    val onClickSearch by remember {
        mutableStateOf(Modifier.clickable {
            navigateToSearchAllInsights()
        })
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        HeadingTextComponentWithoutFullWidth(
            value = title,
            textSize = 18.ssp,
            MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
        Image(
            modifier = Modifier
                .size(22.sdp)
                .then(onClickSearch),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
    }


}
package com.cp.brittany.dixon.ui.components

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
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun ViewAllWorkoutsComponent(
    modifier: Modifier, text: String, showBackButton: Boolean = true,
    navigateToViewAllData: () -> Unit,
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            navigateToViewAllData()
        })
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(click)
            .fillMaxWidth()
    ) {
        BoldTextComponent(
            value = text,
            textSize = 15.ssp,
            textColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
        if (showBackButton) {
            Image(
                modifier = Modifier.size(20.sdp),
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }

    }

}
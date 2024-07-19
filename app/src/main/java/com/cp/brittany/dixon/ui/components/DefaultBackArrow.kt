package com.cp.brittany.dixon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp

@Composable
fun DefaultBackArrow(
    onClick: () -> Unit
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            onClick()
        })
    }
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .then(click),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "Arrow Back",
            modifier = Modifier.size(20.sdp)
        )
    }
}
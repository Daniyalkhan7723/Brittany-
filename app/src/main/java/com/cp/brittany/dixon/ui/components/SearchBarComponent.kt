package com.cp.brittany.dixon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun SearchBarComponent(modifier: Modifier = Modifier) {
    val click by remember {
        mutableStateOf(Modifier.clickable {

        })
    }
    Box(
        modifier = modifier
            .height(45.sdp)
            .border(
                width = 1.sdp,
                color = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(percent = 50)
            )
            .background(
                color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(48.sdp)
            )

            // To make the ripple round
            .clip(shape = RoundedCornerShape(percent = 50))
            .then(click),
        contentAlignment = Alignment.Center
    ) {


    }




}
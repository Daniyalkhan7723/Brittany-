package com.cp.brittany.dixon.ui.screens.home.profile.profileComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun ProfileTopBar(
    title: String,
    backPress: () -> Unit,
    showBackButtonText: Boolean = true,
    modifier: Modifier = Modifier
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            backPress()
        })
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 10.sdp
            ),
    ) {
        Row(
            modifier = Modifier
                .then(click)
                .padding(top = 2.sdp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.rotate(180f).size(18.sdp),

                )

            if (showBackButtonText) {
                NormalTextComponentWithoutFullWidth(
                    stringResource(id = R.string.profile), textSize = 13.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier,
                )
            }

        }

        HeadingTextComponentWithoutFullWidth(
            value = title, textSize = 17.ssp,
            textColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.align(Alignment.Center),
        )

    }
}
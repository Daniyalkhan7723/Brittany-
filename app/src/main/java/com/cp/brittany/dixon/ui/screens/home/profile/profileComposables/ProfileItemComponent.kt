package com.cp.brittany.dixon.ui.screens.home.profile.profileComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.android.billingclient.api.Purchase
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.auth.User
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.ssp
import com.cp.brittany.dixon.utills.sdp


@Composable
fun ProfileItem(
    icon: Int,
    goProfileScreen: () -> Unit,
    modifier: Modifier = Modifier,
    screenType: String,
    clickModifier: Modifier = Modifier,
    data: User?
) {
    val context = LocalContext.current
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    val imageRequest = ImageRequest.Builder(context)
        .data(data?.avatar)
        .listener(listener)
//        .memoryCacheKey(data?.avatar)
//        .diskCacheKey(data?.avatar)
        .placeholder(R.drawable.place_holder)
        .error(R.drawable.place_holder)
        .fallback(R.drawable.place_holder)
//        .diskCachePolicy(CachePolicy.ENABLED)
//        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .build()

    val click by remember {
        mutableStateOf(Modifier.clickable {
            if (screenType == Constants.PROFILE_SETTING_SCREEN) {
                goProfileScreen()
            }
        })
    }

    Box(
        modifier
            .then(click)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            if (screenType == Constants.PROFILE_SETTING_SCREEN) {
                Modifier
                    .padding(vertical = 10.sdp, horizontal = 15.sdp)
                    .fillMaxWidth()
            } else {
                Modifier
                    .padding(start = 15.sdp, end = 15.sdp, top = 45.sdp, bottom = 15.sdp)
                    .fillMaxWidth()
            }

        ) {
            // Load and display the image with AsyncImage
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .size(58.sdp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(start = 10.dp, top = 10.sdp),
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = data?.name ?: "NA",
                        modifier = Modifier.padding(start = 4.sdp),
                        style = TextStyle(
                            fontSize = 17.ssp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(2.sdp))
                    Text(
                        text = data?.email ?: "NA",
                        modifier = Modifier.padding(start = 4.sdp),
                        style = TextStyle(
                            fontSize = 13.ssp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = clickModifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 2.sdp)
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(25.sdp), contentDescription = "setting"
                    )
                }

            }

        }
    }
}
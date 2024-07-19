package com.cp.brittany.dixon.ui.screens.home.insight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.allInsights.InsightDetailsUIEvent
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.htmlText
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.insight.InsightsDetailViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.getFormattedDateWithSmallMin
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import org.json.JSONObject

@Composable
fun InsightDetailScreen(
    onBackPress: () -> Unit,
    sharedViewModel: SharedViewModel,
    insightsDetailViewModel: InsightsDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val addToFavouriteResponse by insightsDetailViewModel.addFavouriteResponse.collectAsStateWithLifecycle()
    val likeResponse by insightsDetailViewModel.likeResponse.collectAsStateWithLifecycle()


    var loaderState by remember {
        mutableStateOf(false)
    }


    val backPressClick by remember {
        mutableStateOf(Modifier.clickable {
            onBackPress()
        })
    }

    val addToFavouritesClick by remember {
        mutableStateOf(Modifier.clickable {
            insightsDetailViewModel.onEvent(
                InsightDetailsUIEvent.AddFavouriteChanged(
                    sharedViewModel.allInsightResponse.value?.id
                )
            )
        })
    }


    val onLikeClick by remember {
        mutableStateOf(Modifier.clickable {
            insightsDetailViewModel.onEvent(
                InsightDetailsUIEvent.LikeChanged(
                    sharedViewModel.allInsightResponse.value?.id
                )
            )
        })
    }



    insightsDetailViewModel.apply {

        LaunchedEffect(key1 = Unit) {
            favouriteImageState.value =
                sharedViewModel.allInsightResponse.value?.is_favourited ?: false
            likeState.value = sharedViewModel.allInsightResponse.value?.likes ?: 0
        }

        if (getScheduleWorkoutsDetailData.value) {
            getScheduleWorkoutsDetailData.value = false
        }

        when (addToFavouriteResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (addToFavouriteResponse.data?.status == true) {
                    addToFavouriteResponse.data?.data?.is_favourited?.let {
                        favouriteImageState.value = it
                    }
                    sharedViewModel.refreshInsightListAfterLikeOrDislike(true)
                    sharedViewModel.refreshFavouriteInsightListAfterLikeOrDislike(true)

                    showToast(
                        title = addToFavouriteResponse.data?.message ?: "", isSuccess = true
                    )
                } else {
                    showToast(
                        title = addToFavouriteResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(addToFavouriteResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    addToFavouriteResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                loaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                loaderState = false
                resetResponse()
                showToast(
                    title = addToFavouriteResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }
        when (likeResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (likeResponse.data?.status == true) {
                    likeState.value = likeResponse.data?.data?.likes ?: 0
                    sharedViewModel.refreshInsightListAfterLikeOrDislike(true)
                    showToast(
                        title = likeResponse.data?.message ?: "", isSuccess = true
                    )
                } else {
                    showToast(
                        title = likeResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(likeResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    likeResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                loaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                loaderState = false
                resetResponse()
                showToast(
                    title = likeResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

    }


    val imageRequest = ImageRequest.Builder(context)
        .data(sharedViewModel.allInsightResponse.value?.thumbnail ?: "")
        .placeholder(R.drawable.workout_place_holder).error(R.drawable.workout_place_holder)
        .fallback(R.drawable.workout_place_holder).crossfade(true).build()


    BrittanyDixonTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.sdp),
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Card(
                shape = RoundedCornerShape(topStart = 15.sdp, topEnd = 15.sdp),
                elevation = CardDefaults.cardElevation(5.dp),
                onClick = {

                },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 290.sdp)
            )
            {
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(start = 15.sdp, end = 15.sdp)) {

                        BoldTextComponent(
                            value = sharedViewModel.allInsightResponse.value?.title
                                ?: "",
                            textSize = 16.ssp,
                            textColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 15.sdp)
                        )

                        Spacer(modifier = Modifier.height(2.sdp))

                        Row(modifier = Modifier.padding(top = 10.sdp)) {
                            Row(modifier = Modifier) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_like),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.size(16.sdp)
                                )
                                Text(
                                    text = "${insightsDetailViewModel.likeState.value} Likes",
                                    style = TextStyle(
                                        fontSize = 12.ssp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(start = 5.sdp)
                                )
                            }

                            Row(modifier = Modifier.padding(start = 8.sdp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_clock),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.size(16.sdp)
                                )
                                Text(
                                    text = sharedViewModel.allInsightResponse.value?.duration?.getFormattedDateWithSmallMin()
                                        ?: "",
                                    style = TextStyle(
                                        fontSize = 12.ssp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                        color = MaterialTheme.colorScheme.onBackground,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(start = 5.sdp)

                                )
                            }
                        }

                        val spannedText = HtmlCompat.fromHtml(
                            sharedViewModel.allInsightResponse.value?.description ?: "",
                            0
                        )

                        htmlText(
                            context, spannedText, textStyle = TextStyle(
                                fontSize = 14.ssp,
                                lineHeight = 5.ssp
                            )
                        )

                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(83.sdp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 25.sdp)
                                .then(onLikeClick),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(modifier = Modifier.padding(start = 30.sdp)) {
                                Image(
                                    modifier = Modifier
                                        .size(15.sdp),
                                    painter = painterResource(id = R.drawable.ic_like_detail),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds
                                )
                                Text(
                                    text = "Like (${insightsDetailViewModel.likeState.value})",
                                    style = TextStyle(
                                        fontSize = 12.ssp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(start = 5.sdp)
                                )
                            }

                            Row(modifier = Modifier.padding(end = 30.sdp)) {
                                Image(
                                    modifier = Modifier
                                        .size(15.sdp),
                                    painter = painterResource(id = R.drawable.ic_share_detail),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds
                                )
                                Text(
                                    text = "Share",
                                    style = TextStyle(
                                        fontSize = 12.ssp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(start = 5.sdp)

                                )
                            }


                        }
                    }
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.sdp)
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 15.sdp)
                        .then(backPressClick)
                        .size(22.sdp),
                )

                Image(
                    painter = if (insightsDetailViewModel.favouriteImageState.value) {
                        painterResource(id = R.drawable.ic_save_fav)
                    } else {
                        painterResource(id = R.drawable.ic_star)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 15.sdp)
                        .then(addToFavouritesClick)
                        .size(22.sdp),
                )

            }


            if (loaderState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Alignment.Center),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,

                    )
            }

        }
    }
}
package com.cp.brittany.dixon.ui.screens.home.insight.insightComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.ui.components.BorderButtonRound
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.getFormattedDateWithSmallMin
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun AllInsightsItem(
    navigateToInsightDetailsScreen: () -> Unit,
    allInsight: AllInsight,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current


    val imageRequest = ImageRequest.Builder(context).data(allInsight.thumbnail)
        .placeholder(R.drawable.place_holder_3).error(R.drawable.place_holder_3)
        .fallback(R.drawable.place_holder_3).crossfade(true).build()

    val onClickItem by remember {
        mutableStateOf(Modifier.clickable {
            navigateToInsightDetailsScreen()
        })
    }

    val onClickReadMore = remember {
        {
            navigateToInsightDetailsScreen()
        }

    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(bottom = 10.sdp)
            .width(295.sdp)
            .then(onClickItem)
            .border(
                width = 1.sdp, color = Color(0xFF31363D), shape = RoundedCornerShape(percent = 5)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(9.sdp)
            ), contentAlignment = Center
    ) {

        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                Modifier
                    .padding(top = 10.sdp, bottom = 10.sdp, start = 10.sdp)
                    .fillMaxWidth()
            ) {
                // Load and display the image with AsyncImage
                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 5.sdp)
                        .width(71.sdp)
                        .height(58.sdp)
                        .clip(RoundedCornerShape(10.sdp)),
                    contentScale = ContentScale.Crop,
                )
                Box(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(start = 10.sdp, top = 8.sdp, end = 10.sdp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = allInsight.title ?: "",
                            modifier = Modifier.padding(start = 4.sdp),
                            maxLines = 1,
                            style = TextStyle(
                                fontSize = 13.ssp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = AppFont.MyCustomFont
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.height(2.sdp))

                        Text(
                            text = allInsight.short_description ?: "",
                            maxLines = 2,
                            modifier = Modifier
                                .heightIn(min = 40.sdp)
                                .padding(start = 4.sdp),
                            style = TextStyle(
                                fontSize = 12.ssp,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontFamily = AppFont.MyCustomFont
                            ), color = MaterialTheme.colorScheme.onSecondaryContainer
                        )


                    }

                }

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.sdp, end = 15.sdp)
                    .height((1.5).dp)
                    .background(Color(0xFF35393e))
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.sdp, bottom = 15.sdp, start = 15.sdp, end = 15.sdp)
                    .fillMaxWidth()
            ) {
                Column {
                    Row(modifier = Modifier) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = "${allInsight.likes.toString()} Likes",
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

                    Row(modifier = Modifier.padding(top = 8.sdp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = allInsight.duration?.getFormattedDateWithSmallMin() ?: "",
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

                BorderButtonRound(
                    value = stringResource(R.string.read_more),
                    onButtonClicked = onClickReadMore,
                    modifier = Modifier
                        .width(103.sdp)
                        .height(30.sdp),
                    shapeRadius = 8,
                    borderRadius = 10,
                    textColor = MaterialTheme.colorScheme.secondary,
                    borderColor = MaterialTheme.colorScheme.secondary
                )

            }


        }
    }
}

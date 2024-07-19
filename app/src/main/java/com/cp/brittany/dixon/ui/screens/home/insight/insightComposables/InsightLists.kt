package com.cp.brittany.dixon.ui.screens.home.insight.insightComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.DetailRoute
import com.cp.brittany.dixon.ui.screens.home.insight.InsightCard
import com.cp.brittany.dixon.ui.viewModel.insight.InsightViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp

@Composable
fun InsightLists(
    modifier: Modifier = Modifier,
    navigateToInsightsDetails: (AllInsight) -> Unit,
    getLatestInsights: MutableList<AllInsight>

) {
    val addToFavourite = remember<(Int) -> Unit> {
        { workoutData ->

        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.sdp),
        modifier = modifier.padding(top = 15.sdp)
    ) {
        items(count = getLatestInsights.size, key = {
            getLatestInsights[it].id ?: 0
        }, itemContent = { index ->
            InsightCard(
                latestInsight = getLatestInsights[index],
                navigateToInsightsDetails = navigateToInsightsDetails,
                modifier = Modifier
                    .height(240.sdp)
                    .width(230.sdp),
                screenType = Constants.INSIGHTS,
                addToFavourite = addToFavourite
            )
        })
    }
}
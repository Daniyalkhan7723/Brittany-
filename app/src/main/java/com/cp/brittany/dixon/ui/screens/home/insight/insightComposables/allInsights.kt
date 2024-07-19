package com.cp.brittany.dixon.ui.screens.home.insight.insightComposables

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.workout.WorkoutData

fun LazyListScope.allInsights(
    allInsightsList: MutableList<AllInsight>,
    navigateToInsightDetailsScreen: (AllInsight) -> Unit,
    modifier: Modifier = Modifier
) {
    items(count = allInsightsList.size, key = {
        allInsightsList[it].id ?: 0
    }, itemContent = { index ->
        val insights = allInsightsList[index]
        var onNavigateToInsightsDetails = remember {
            {
                navigateToInsightDetailsScreen(allInsightsList[index])
            }
        }
        AllInsightsItem(
            navigateToInsightDetailsScreen = onNavigateToInsightsDetails,
            allInsight = insights,
            modifier = modifier
        )
    })
}
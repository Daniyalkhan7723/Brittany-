package com.cp.brittany.dixon.utills.composecalendar.month

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.composecalendar.day.DayState
import com.cp.brittany.dixon.utills.composecalendar.header.MonthState
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionState
import com.cp.brittany.dixon.utills.composecalendar.week.WeekRow
import com.cp.brittany.dixon.utills.composecalendar.week.getWeeks
import com.cp.brittany.dixon.utills.sdp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("LongMethod")
internal fun <T : SelectionState> MonthPager(
    initialMonth: YearMonth,
    showAdjacentMonths: Boolean,
    selectionState: T,
    monthState: MonthState,
    daysOfWeek: List<DayOfWeek>,
    today: LocalDate,
    modifier: Modifier = Modifier,
    weekDaysScrollEnabled: Boolean = true,
    dayContent: @Composable BoxScope.(DayState<T>) -> Unit,
    weekHeader: @Composable BoxScope.(List<DayOfWeek>) -> Unit,
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit,
    screenType: String,

    ) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (screenType == Constants.SCHEDULE_WORKOUTS) SecondStartIndex else StartIndex
    )

    val monthListState = remember {
        MonthListState(
            coroutineScope = coroutineScope,
            initialMonth = initialMonth,
            monthState = monthState,
            listState = listState,
            screenType = screenType
        )
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.sdp, end = 15.sdp)
    ) {
        LazyRow(
            modifier = modifier.testTag("MonthPager"),
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            verticalAlignment = Alignment.Top,
        ) {
            items(PagerItemCount) { index ->
                MonthContent(
                    modifier = Modifier.fillParentMaxWidth(),
                    showAdjacentMonths = showAdjacentMonths,
                    selectionState = selectionState,
                    currentMonth = monthListState.getMonthForPage(index),
                    today = today,
                    weekDaysScrollEnabled = weekDaysScrollEnabled,
                    daysOfWeek = daysOfWeek,
                    dayContent = dayContent,
                    weekHeader = weekHeader,
                    monthContainer = monthContainer
                )
            }
        }
    }
}


@Composable
internal fun <T : SelectionState> MonthContent(
    showAdjacentMonths: Boolean,
    selectionState: T,
    currentMonth: YearMonth,
    daysOfWeek: List<DayOfWeek>,
    today: LocalDate,
    modifier: Modifier = Modifier,
    weekDaysScrollEnabled: Boolean = true,
    dayContent: @Composable BoxScope.(DayState<T>) -> Unit,
    weekHeader: @Composable BoxScope.(List<DayOfWeek>) -> Unit,
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit,
) {
    Column {
        if (weekDaysScrollEnabled) {
            Box(
                modifier = modifier.wrapContentHeight(),
                content = { weekHeader(daysOfWeek) },
            )
        }
        monthContainer { paddingValues ->
            Column(
                modifier = modifier.padding(paddingValues)
            ) {

                currentMonth.getWeeks(
                    includeAdjacentMonths = showAdjacentMonths,
                    firstDayOfTheWeek = daysOfWeek.first(),
                    today = today,
                ).forEach { week ->
                    WeekRow(
                        weekDays = week,
                        selectionState = selectionState,
                        dayContent = dayContent,
                    )
                }

            }
        }
    }
}

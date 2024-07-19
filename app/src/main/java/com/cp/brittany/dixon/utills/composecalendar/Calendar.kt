@file:Suppress("MatchingDeclarationName")

package com.cp.brittany.dixon.utills.composecalendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.ButtonComponentNoFullWidth
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.composecalendar.day.DayState
import com.cp.brittany.dixon.utills.composecalendar.day.DefaultDay
import com.cp.brittany.dixon.utills.composecalendar.header.DefaultMonthHeader
import com.cp.brittany.dixon.utills.composecalendar.header.MonthState
import com.cp.brittany.dixon.utills.composecalendar.month.MonthPager
import com.cp.brittany.dixon.utills.composecalendar.selection.DynamicSelectionState
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionMode
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionState
import com.cp.brittany.dixon.utills.composecalendar.week.DaysInAWeek
import com.cp.brittany.dixon.utills.composecalendar.week.DefaultDaysOfWeekHeader
import com.cp.brittany.dixon.utills.composecalendar.week.rotateRight
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.sheets.BottomSheetState
import com.cp.brittany.dixon.utills.ssp
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

@Stable
class CalendarState<T : SelectionState>(
    val monthState: MonthState,
    val selectionState: T,
)

@Composable
fun SelectableCalendar(
    modifier: Modifier = Modifier,
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
    today: LocalDate = LocalDate.now(),
    showAdjacentMonths: Boolean = true,
    weekDaysScrollEnabled: Boolean = true,
    screenType: String,
    calendarState: CalendarState<DynamicSelectionState> = rememberSelectableCalendarState(),
    dayContent: @Composable BoxScope.(DayState<DynamicSelectionState>) -> Unit =
        {
            DefaultDay(
                it, screenType = screenType,
            )
        },
    monthHeader: @Composable ColumnScope.(MonthState) -> Unit = {
        DefaultMonthHeader(it, screenType = screenType)
    },
    daysOfWeekHeader: @Composable BoxScope.(List<DayOfWeek>) -> Unit = { DefaultDaysOfWeekHeader(it) },
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit = { content ->
        Box { content(PaddingValues()) }
    },
    state: BottomSheetState,
    onClickSave: () -> Unit,
    startEndDate: (String, String) -> Unit,

    ) {

    val scope = rememberCoroutineScope()

    val dismissListener by remember {
        mutableStateOf(Modifier.clickable {
            scope.launch { state.collapse() }
        })
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 18.sdp, end = 15.sdp)
        ) {
            BoldTextComponent(
                value = stringResource(R.string.select_date),
                textSize = 15.ssp,
                textColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )
            Image(
                modifier = Modifier
                    .size(23.sdp)
                    .then(dismissListener),
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }

        Spacer(
            modifier = Modifier
                .padding(top = 15.sdp, bottom = 10.sdp)
                .height((1).sdp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        val startDate =
            Utils.getMonthStartEndDate(calendarState.monthState.currentMonth.toString()).first
        val endDate =
            Utils.getMonthStartEndDate(calendarState.monthState.currentMonth.toString()).second
        startEndDate(startDate, endDate)

        Calendar(
            modifier = modifier,
            firstDayOfWeek = firstDayOfWeek,
            today = today,
            showAdjacentMonths = showAdjacentMonths,
            weekDaysScrollEnabled = weekDaysScrollEnabled,
            calendarState = calendarState,
            dayContent = dayContent,
            monthHeader = monthHeader,
            daysOfWeekHeader = daysOfWeekHeader,
            monthContainer = monthContainer,
            screenType = screenType,
//            startEndDate = startEndDate
        )


        if (screenType == Constants.WORKOUT_DETAIL_SCREEN) {
            ButtonComponentNoFullWidth(
                value = stringResource(R.string.save),
                onButtonClicked = {
                    onClickSave()
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(bottom = 10.sdp, top = 5.sdp)
                    .heightIn(45.sdp),
                buttonColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.background,
            )
        }

    }

}


@Composable
fun <T : SelectionState> Calendar(
    calendarState: CalendarState<T>,
    modifier: Modifier = Modifier,
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
    today: LocalDate = LocalDate.now(),
    showAdjacentMonths: Boolean = true,
    weekDaysScrollEnabled: Boolean = true,
    screenType: String,
    dayContent: @Composable BoxScope.(DayState<T>) -> Unit = {
        DefaultDay(
            it, screenType = screenType,
        )
    },
    monthHeader: @Composable ColumnScope.(MonthState) -> Unit = {
        DefaultMonthHeader(
            it, screenType = screenType
        )
    },
    daysOfWeekHeader: @Composable BoxScope.(List<DayOfWeek>) -> Unit = { DefaultDaysOfWeekHeader(it) },
    monthContainer: @Composable (content: @Composable (PaddingValues) -> Unit) -> Unit = { content ->
        Box { content(PaddingValues()) }
    },
) {
    val initialMonth = remember { calendarState.monthState.currentMonth }
    val daysOfWeek = remember(firstDayOfWeek) {
        DayOfWeek.values().rotateRight(DaysInAWeek - firstDayOfWeek.ordinal)
    }

    Column(
        modifier = modifier,
    ) {
        monthHeader(calendarState.monthState)
        Spacer(modifier = Modifier.height(15.sdp))
        MonthPager(
            initialMonth = initialMonth,
            showAdjacentMonths = showAdjacentMonths,
            monthState = calendarState.monthState,
            selectionState = calendarState.selectionState,
            today = today,
            weekDaysScrollEnabled = weekDaysScrollEnabled,
            daysOfWeek = daysOfWeek,
            dayContent = dayContent,
            weekHeader = daysOfWeekHeader,
            monthContainer = monthContainer,
            screenType = screenType,
        )
    }
}

@Composable
fun rememberSelectableCalendarState(
    initialMonth: YearMonth = YearMonth.now(),
    initialSelection: List<LocalDate> = emptyList(),
    initialSelectionMode: SelectionMode = SelectionMode.Single,
    confirmSelectionChange: (newValue: List<LocalDate>) -> Boolean = { true },
    monthState: MonthState = rememberSaveable(saver = MonthState.Saver()) {
        MonthState(initialMonth = initialMonth)
    },
    selectionState: DynamicSelectionState = rememberSaveable(
        saver = DynamicSelectionState.Saver(confirmSelectionChange),
    ) {
        DynamicSelectionState(confirmSelectionChange, initialSelection, initialSelectionMode)
    },
): CalendarState<DynamicSelectionState> = remember { CalendarState(monthState, selectionState) }

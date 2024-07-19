package com.cp.brittany.dixon.ui.screens.home.calendar

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.MonthlyCalendarUIEvent
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables.ScheduledWorkoutItem
import com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables.WeekDayCalendarItem
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutTopBar
import com.cp.brittany.dixon.ui.viewModel.calendar.AllScheduleWorkoutsViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.Utils.convertStringToDate
import com.cp.brittany.dixon.utills.Utils.parseDateString
import com.cp.brittany.dixon.utills.composecalendar.CalendarUiModel
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.sheets.rememberBottomSheetState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllScheduleWorkoutsScreen(
    onBackPress: () -> Unit,
    allScheduleWorkoutsViewModel: AllScheduleWorkoutsViewModel = hiltViewModel(),
    navigateToScheduleWorkoutsDetailScreen: (WorkoutData) -> Unit,

    ) {
    allScheduleWorkoutsViewModel.apply {
        val systemUiController = rememberSystemUiController()
        val state = rememberPullToRefreshState()
        val bottomSheetState = rememberBottomSheetState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val getScheduleWorkoutResponse by workoutByPreviousDatesResponse.collectAsStateWithLifecycle()
        val getScheduleSlotsDateResponse by getScheduleSlotsResponse.collectAsStateWithLifecycle()
        val getScheduleSlotsDateForCalendarResponse by getScheduleSlotsCalendarResponse.collectAsStateWithLifecycle()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )

        val backPress = remember {
            {
                onBackPress()
            }
        }


        val startEndDate = remember<(String, String) -> Unit> {
            { startDate, endDate ->
                if (!preventAgainApiCall) {
                    preventAgainApiCall = true
                    onEvent(
                        MonthlyCalendarUIEvent.SelectStartEndDateFromCalendar(
                            startDate, endDate
                        )
                    )
                }

            }
        }


        val navigateToWorkoutSeeMore = remember {
            {

            }
        }

        val selectedDates = remember<(String) -> Unit> {
            { date ->
                scope.launch {
                    if (date != "") {
                        monthDates.value = dataSource.getData(
                            lastSelectedDate = dataSource.today, startDate = parseDateString(date)
                        )
                        monthDates.value =
                            monthDates.value.copy(visibleDates = monthDates.value.visibleDates.map {
                                it.copy(
                                    isSelected = it.date.isEqual(convertStringToDate(date))
                                )
                            })

                        bottomSheetState.collapse()

                        getScheduleWorkoutsResponse.value = true
                        visibleScheduleItems.value = false

                        onEvent(
                            MonthlyCalendarUIEvent.SelectDateFromWeekCalendar(
                                date
                            )
                        )
                        onEvent(
                            MonthlyCalendarUIEvent.GetSlotsForWeekCalendar(
                                monthDates.value.startDate.date.toString(),
                                monthDates.value.endDate.date.toString()
                            )
                        )


                    }

                }
            }
        }

        val onSelectDateFromWeekCalendar = remember<(CalendarUiModel.Date) -> Unit> {
            { date ->
                getScheduleWorkoutsResponse.value = true
                visibleScheduleItems.value = false
                onEvent(
                    MonthlyCalendarUIEvent.SelectDateFromWeekCalendar(
                        date.date.toString()
                    )
                )
                monthDates.value = monthDates.value.copy(selectedDate = date,
                    visibleDates = monthDates.value.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    })
            }
        }


        var showNoDataFoundOrNot by remember {
            mutableStateOf(false)
        }

        var swipeRefresh by remember {
            mutableStateOf(false)
        }


        val onClickOpenCalendar by remember {
            mutableStateOf(Modifier.clickable {
                scope.launch { bottomSheetState.expand() }
            })
        }

        LaunchedEffect(key1 = Unit) {
            getScheduleWorkoutsSlotsResponse.value = true
            getScheduleWorkoutsResponse.value = true
        }

        if (state.isRefreshing) {
            LaunchedEffect(true) {
                swipeRefresh = true
                getScheduleWorkoutsResponse.value = true
                getScheduleWorkoutsSlotsResponse.value = true
                onEvent(
                    MonthlyCalendarUIEvent.RefreshData
                )

            }
        }

        if (getScheduleWorkoutsSlotsResponse.value) {      // For avoid recomposition
            when (getScheduleSlotsDateResponse) {
                is NetworkResult.Success<*> -> {
                    getScheduleWorkoutsSlotsResponse.value = false
                    if (getScheduleSlotsDateResponse.data?.status == true) {
                        getAvailableSlots.clear()
                        getScheduleSlotsDateResponse.data?.data?.let {
                            getAvailableSlots.addAll(it)
                        }
                    } else {
                        showToast(
                            title = getScheduleSlotsDateResponse.data?.message ?: "",
                            isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    getScheduleWorkoutsSlotsResponse.value = false
                    resetResponse()
                    val message: String = try {
                        val jObjError = JSONObject(getScheduleSlotsDateResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        getScheduleSlotsDateResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    getScheduleWorkoutsSlotsResponse.value = false
                    resetResponse()
                    showToast(
                        title = getScheduleSlotsDateResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }


        when (getScheduleSlotsDateForCalendarResponse) {
            is NetworkResult.Success<*> -> {
                if (getScheduleSlotsDateForCalendarResponse.data?.status == true) {
                    workoutByDateLoaderState = false
                    preventAgainApiCall = false
                    getAvailableSlotsForCalendar.clear()
                    getScheduleSlotsDateForCalendarResponse.data?.data?.let {
                        getAvailableSlotsForCalendar.addAll(it)
                    }
                } else {
                    showToast(
                        title = getScheduleSlotsDateForCalendarResponse.data?.message ?: "",
                        isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                workoutByDateLoaderState = false
                getScheduleWorkoutsSlotsResponse.value = false
                resetResponse()
                val message: String = try {
                    val jObjError =
                        JSONObject(getScheduleSlotsDateForCalendarResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    getScheduleSlotsDateForCalendarResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                if (!swipeRefresh) workoutByDateLoaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                workoutByDateLoaderState = false
                getScheduleWorkoutsSlotsResponse.value = false
                resetResponse()
                showToast(
                    title = getScheduleSlotsDateForCalendarResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        if (getScheduleWorkoutsResponse.value) {  // For avoid recomposition
            when (getScheduleWorkoutResponse) {
                is NetworkResult.Success<*> -> {
                    workoutByDateLoaderState = false
                    getScheduleWorkoutsResponse.value = false
                    state.endRefresh()
                    if (getScheduleWorkoutResponse.data?.status == true) {
                        visibleScheduleItems.value = true
                        scheduleWorkoutResponseData.clear()

                        (getScheduleWorkoutResponse.data?.data?.isEmpty()
                            ?: false).also { showNoDataFoundOrNot = it }
                        getScheduleWorkoutResponse.data?.data?.let {
                            scheduleWorkoutResponseData.addAll(it)
                        }
                    } else {
                        visibleScheduleItems.value = false
                        showToast(
                            title = getScheduleWorkoutResponse.data?.message ?: "",
                            isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    state.endRefresh()
                    getScheduleWorkoutsResponse.value = false
                    visibleScheduleItems.value = false
                    workoutByDateLoaderState = false
                    resetResponse()
                    val message: String = try {
                        val jObjError = JSONObject(getScheduleWorkoutResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        getScheduleWorkoutResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    if (!swipeRefresh) workoutByDateLoaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    state.endRefresh()
                    getScheduleWorkoutsResponse.value = false
                    visibleScheduleItems.value = false
                    workoutByDateLoaderState = false
                    resetResponse()
                    showToast(
                        title = getScheduleWorkoutResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 30.sdp)
                .nestedScroll(state.nestedScrollConnection)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
            ) {
                WorkoutTopBar(
                    title = stringResource(R.string.workouts),
                    backPress = backPress,
                    searchClick = navigateToWorkoutSeeMore,
                    endIcon = R.drawable.ic_calendar_unselected,
                    modifier = Modifier.then(onClickOpenCalendar),
                    screenType = Constants.SCHEDULE_WORKOUTS,
                    backIconText = stringResource(id = R.string.calendar)
                )

                Spacer(modifier = Modifier.height(15.sdp))

                LazyRow(modifier = Modifier.padding(horizontal = 5.sdp)) {
                    items(monthDates.value.visibleDates.size) { index ->
                        var dateMatched = false
                        getAvailableSlots.forEach { sloteSate ->
                            if (sloteSate.date == monthDates.value.visibleDates[index].date.toString()) {
                                dateMatched = true
                                return@forEach
                            }
                        }

                        WeekDayCalendarItem(
                            showSlots = dateMatched,
                            date = monthDates.value.visibleDates[index],
                            onClickListener = onSelectDateFromWeekCalendar
                        )
                    }
                }

                if (visibleScheduleItems.value) {
                    LazyColumn {
                        val data = scheduleWorkoutResponseData
                        items(count = data.size, key = {
                            data[it].id ?: 0
                        }, itemContent = { index ->
                            val workouts = data[index]
                            ScheduledWorkoutItem(index = index,
                                listSize = data.size,
                                workoutByDate = workouts,
                                navigateToScheduleWorkoutsDetailsScreen = {
                                    navigateToScheduleWorkoutsDetailScreen(data[index])
                                })
                        })
                    }
                }

                DateCalendarBottomSheet(
                    state = bottomSheetState,
                    screenType = Constants.SCHEDULE_WORKOUTS,
                    selectDate = selectedDates,
                    startEndDate = startEndDate,
                    slotsList = getAvailableSlotsForCalendar
                )


            }
            if (showNoDataFoundOrNot) {
                NoDataFound(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (workoutByDateLoaderState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Alignment.Center),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            PullToRefreshContainer(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
            )

        }
    }
}
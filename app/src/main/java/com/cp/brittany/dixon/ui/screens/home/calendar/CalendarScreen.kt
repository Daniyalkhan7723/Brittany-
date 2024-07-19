package com.cp.brittany.dixon.ui.screens.home.calendar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables.TodayActivityItem
import com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables.WeekDayCalendarItem
import com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables.scheduleWorkouts
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.calendar.CalendarViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.composecalendar.CalendarUiModel
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    navigateToAllScheduledWorkoutsScreen: () -> Unit,
    navigateToScheduleWorkoutsDetailScreen: (WorkoutData) -> Unit,
    sharedViewModel: SharedViewModel
) {
    calendarViewModel.apply {
        val systemUiController = rememberSystemUiController()
        val state = rememberPullToRefreshState()
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = 0,
        )

        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        val context = LocalContext.current

        val getTodayScheduleActivityResponse by workoutByDatesResponse.collectAsStateWithLifecycle()
        val getScheduleSlotsDateResponse by getScheduleSlotsResponse.collectAsStateWithLifecycle()
        val getWorkoutByPreviousDateResponse by workoutByPreviousDatesResponse.collectAsStateWithLifecycle()
        val addMultipleImageResponse by addMultipleImageResponse.collectAsStateWithLifecycle()
        val sharedViewModelResponseForSchedule by sharedViewModel.refreshScheduleWorkoutsResponse.collectAsStateWithLifecycle()
        val refreshDetailScreen by sharedViewModel.refreshDetailScreen.collectAsStateWithLifecycle()




        LaunchedEffect(key1 = Unit) {
            offsetY1.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetY2.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetX3.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetY3.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }


        val onNavigateToAllScheduledWorkoutsScreen = remember {
            {
                navigateToAllScheduledWorkoutsScreen()
            }
        }

        var swipeRefresh by remember {
            mutableStateOf(false)
        }


        val getUriList = remember<(List<String>, Int, Int) -> Unit> {
            { list, workoutId, index ->
                indexValue.value = index
                onEvent(
                    CalendarUIEvent.GetImages(
                        list, workoutId
                    )
                )

            }
        }

        val onSelectDateFromWeekCalendar = remember<(CalendarUiModel.Date) -> Unit> {
            { date ->
                onEvent(
                    CalendarUIEvent.SelectDateFromWeekCalendar(
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

        if (state.isRefreshing) {
            LaunchedEffect(true) {
                swipeRefresh = true
                getScheduleWorkoutsResponse.value = true
                getAllScheduledWorkoutsResponse.value = true
                getScheduleWorkoutsSlotsResponse.value = true
                onEvent(
                    CalendarUIEvent.RefreshData
                )
            }
        }

        LaunchedEffect(key1 = Unit) {
            when (sharedViewModelResponseForSchedule) {
                true -> {
                    workoutByDates()
                    sharedViewModel.resetResponse()
                }

                false -> {

                }
            }

            when (refreshDetailScreen) {
                true -> {
                    workoutByDates()
                    sharedViewModel.resetResponseForRefreshDetailScreen()
                }

                false -> {

                }
            }
            getScheduleWorkoutsResponse.value = true
            getAllScheduledWorkoutsResponse.value = true
            getScheduleWorkoutsSlotsResponse.value = true
        }

        if (getScheduleWorkoutsResponse.value) {
            when (getTodayScheduleActivityResponse) {
                is NetworkResult.Success<*> -> {
                    getScheduleWorkoutsResponse.value = false
                    if (getTodayScheduleActivityResponse.data?.status == true) {
                        scheduleWorkoutResponseData.clear()
                        getTodayScheduleActivityResponse.data?.data?.let {
                            if (it.isNotEmpty()) {
                                visibleTodayScheduleItems.value = true
                            }
                            checkTodayScheduleWorkoutsAvailableOrNot = true
                            scheduleWorkoutResponseData.addAll(it)
                        }
                    } else {
                        visibleTodayScheduleItems.value = false
                        showToast(
                            title = getTodayScheduleActivityResponse.data?.message ?: "",
                            isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    state.endRefresh()
                    visibleTodayScheduleItems.value = false
                    workoutByDateLoaderState = false
                    visibleScheduleItems.value = false
                    getScheduleWorkoutsResponse.value = false
                    resetResponse()
                    val message: String = try {
                        val jObjError =
                            JSONObject(getTodayScheduleActivityResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        getTodayScheduleActivityResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    if (!swipeRefresh)
                        workoutByDateLoaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    state.endRefresh()
                    visibleTodayScheduleItems.value = false
                    visibleScheduleItems.value = false
                    getScheduleWorkoutsResponse.value = false
                    workoutByDateLoaderState = false
                    resetResponse()
                    showToast(
                        title = getTodayScheduleActivityResponse.message ?: "",
                        isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }

        if (getScheduleWorkoutsSlotsResponse.value) {
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
                    state.endRefresh()
                    workoutByDateLoaderState = false
                    visibleScheduleItems.value = false
                    getScheduleWorkoutsSlotsResponse.value = false
                    resetResponse()
                    val message: String = try {
                        val jObjError =
                            JSONObject(getScheduleSlotsDateResponse.message.toString())
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
                    if (!swipeRefresh)
                        workoutByDateLoaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    state.endRefresh()
                    visibleScheduleItems.value = false
                    getScheduleWorkoutsSlotsResponse.value = false
                    workoutByDateLoaderState = false
                    resetResponse()
                    showToast(
                        title = getScheduleSlotsDateResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }

        if (getAllScheduledWorkoutsResponse.value) {
            when (getWorkoutByPreviousDateResponse) {
                is NetworkResult.Success<*> -> {
                    workoutByDateLoaderState = false
                    state.endRefresh()
                    getAllScheduledWorkoutsResponse.value = false
                    if (getWorkoutByPreviousDateResponse.data?.status == true) {
                        visibleScheduleItems.value = true
                        showScheduleWorkoutList.value = true
                        previousScheduleWorkoutResponseData.clear()

                        if (checkTodayScheduleWorkoutsAvailableOrNot) {
                            (getWorkoutByPreviousDateResponse.data?.data?.isEmpty() == true).also {
                                showNoDataFoundIfHasTodayWorkouts = it
                            }
                        } else {
                            (getWorkoutByPreviousDateResponse.data?.data?.isEmpty() == true).also {
                                showNoDataFoundOrNot = it
                            }

                        }
                        getWorkoutByPreviousDateResponse.data?.data?.let {
                            previousScheduleWorkoutResponseData.addAll(it)
                        }
                    } else {
                        visibleScheduleItems.value = false
                        showToast(
                            title = getWorkoutByPreviousDateResponse.data?.message ?: "",
                            isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    state.endRefresh()
                    workoutByDateLoaderState = false
                    visibleScheduleItems.value = false
                    getAllScheduledWorkoutsResponse.value = false
                    resetResponse()
                    val message: String = try {
                        val jObjError =
                            JSONObject(getWorkoutByPreviousDateResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        getWorkoutByPreviousDateResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    if (!swipeRefresh)
                        workoutByDateLoaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    state.endRefresh()
                    visibleScheduleItems.value = false
                    workoutByDateLoaderState = false
                    getAllScheduledWorkoutsResponse.value = false
                    resetResponse()
                    showToast(
                        title = getWorkoutByPreviousDateResponse.message ?: "",
                        isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }


        when (addMultipleImageResponse) {
            is NetworkResult.Success<*> -> {
                workoutByDateLoaderState = false
                if (addMultipleImageResponse.data?.status == true) {
                    addMultipleImageResponse.data?.data?.let {
                        showToast(
                            title = addMultipleImageResponse.data?.message ?: "",
                            isSuccess = true
                        )
                        updateSelection(indexValue.value, it.images)
                    }
                } else {
                    visibleTodayScheduleItems.value = false
                    showToast(
                        title = addMultipleImageResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                workoutByDateLoaderState = false
                val message: String = try {
                    val jObjError = JSONObject(addMultipleImageResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    addMultipleImageResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }
                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                workoutByDateLoaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                workoutByDateLoaderState = false
                resetResponse()
                showToast(
                    title = addMultipleImageResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        Box(
            modifier = Modifier
                .nestedScroll(state.nestedScrollConnection)
                .padding(top = 40.sdp, bottom = 40.sdp)
                .fillMaxSize()

        ) {
            if (visibleScheduleItems.value) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        HeadingTextComponentWithoutFullWidth(value = stringResource(R.string.calendar),
                            textSize = 20.ssp,
                            textColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(start = 15.sdp)
                                .offset {
                                    IntOffset(
                                        offsetX2.value.toInt(), offsetY2.value.toInt()
                                    )
                                })
                        if (visibleTodayScheduleItems.value) {
                            Spacer(
                                modifier = Modifier
                                    .height(15.sdp)
                                    .padding(start = 10.sdp)
                            )
                            BoldTextComponent(value = stringResource(R.string.today_s_activity),
                                textSize = 16.ssp,
                                textColor = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(start = 15.sdp)
                                    .offset {
                                        IntOffset(
                                            offsetX2.value.toInt(), offsetY2.value.toInt()
                                        )
                                    }

                            )

                            Spacer(modifier = Modifier.height(15.sdp))

                            LazyRow(
                                modifier = Modifier
                                    .padding(start = 5.sdp)
                                    .offset {
                                        IntOffset(
                                            offsetX1.value.toInt(), offsetY1.value.toInt()
                                        )
                                    },
                                horizontalArrangement = Arrangement.spacedBy(5.sdp),
                                state = listState,
                                flingBehavior = rememberSnapFlingBehavior(listState),
                                verticalAlignment = Alignment.Top,
                            ) {
                                items(count = scheduleWorkoutResponseData.size, key = {
                                    scheduleWorkoutResponseData[it].id ?: 0
                                }, itemContent = { index ->
                                    TodayActivityItem(
                                        navigateToScheduleWorkoutsDetailsScreen = {
                                            navigateToScheduleWorkoutsDetailScreen(
                                                scheduleWorkoutResponseData[index]
                                            )
                                        },
                                        workoutByDate = scheduleWorkoutResponseData[index],
                                        listUri = getUriList,
                                        index = index
                                    )
                                })
                            }

                            Spacer(modifier = Modifier
                                .offset {
                                    IntOffset(
                                        offsetX1.value.toInt(), offsetY1.value.toInt()
                                    )
                                }
                                .fillMaxWidth()
                                .padding(top = 30.sdp)
                                .height((2).sdp)
                                .background(Color(0xFF35393e)))
                        }


                        Column(modifier = Modifier.offset {
                            IntOffset(
                                offsetX3.value.toInt(), offsetY3.value.toInt()
                            )
                        })
                        {
                            ViewAllWorkoutsComponent(
                                modifier = Modifier.padding(
                                    start = 15.sdp, end = 10.sdp, top = 15.sdp
                                ),
                                text = stringResource(R.string.explore_previous_workouts),
                                showBackButton = true,
                                navigateToViewAllData = onNavigateToAllScheduledWorkoutsScreen
                            )
                            Spacer(modifier = Modifier.padding(8.sdp))

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

                            if (showNoDataFoundIfHasTodayWorkouts) {
                                Column(
                                    modifier = Modifier
                                        .align(CenterHorizontally)
                                        .padding(top = 30.sdp),
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_no_data_found),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                            .size(60.sdp)
                                    )

                                    BoldTextComponent(
                                        value = stringResource(R.string.no_data_found),
                                        textSize = 14.ssp,
                                        textColor = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                    )

                                }

                            }
                        }


                    }



                    if (showScheduleWorkoutList.value) {
                        scheduleWorkouts(workoutByDateResponseData = previousScheduleWorkoutResponseData,
                            navigateToScheduleWorkoutsDetailsScreen = navigateToScheduleWorkoutsDetailScreen,
                            modifier = Modifier.offset {
                                IntOffset(
                                    offsetX3.value.toInt(), offsetY3.value.toInt()
                                )
                            })

                    }

                    item {
                        Spacer(modifier = Modifier.height(50.sdp))
                    }

                }
            }

            if (showNoDataFoundOrNot) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_no_data_found),
                        contentDescription = null,
                        modifier = Modifier.align(CenterHorizontally)
                    )

                    BoldTextComponent(
                        value = stringResource(R.string.no_data_found),
                        textSize = 14.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .align(CenterHorizontally)
                    )

                }

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



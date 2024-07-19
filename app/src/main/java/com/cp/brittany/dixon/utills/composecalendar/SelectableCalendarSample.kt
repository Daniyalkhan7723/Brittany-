package com.cp.brittany.dixon.utills.composecalendar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.composecalendar.day.DefaultDay
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionMode
import com.cp.brittany.dixon.utills.sheets.BottomSheetState

@Composable
fun SelectableCalendarSample(
    state: BottomSheetState, screenType: String,
    selectDate: (String) -> Unit,
    startEndDate: (String, String) -> Unit,
    slotsList: MutableList<WorkoutData> = ArrayList()
) {

    val calendarState = rememberSelectableCalendarState()
    val onSaveClick = remember {
        {
            selectDate(calendarState.selectionState.selection.joinToString { it.toString() })
        }
    }

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        if (screenType == Constants.SCHEDULE_WORKOUTS) {
            calendarState.selectionState.selectionMode = SelectionMode.Single
            selectDate(calendarState.selectionState.selection.joinToString { it.toString() })
        } else {
            calendarState.selectionState.selectionMode = SelectionMode.Multiple
        }

        SelectableCalendar(
            calendarState = calendarState,
            modifier = Modifier.animateContentSize(),
            state = state,
            screenType = screenType,
            onClickSave = onSaveClick,
            startEndDate = startEndDate,
            dayContent = {
                DefaultDay(
                    it, screenType = screenType,
                    slots = slotsList
                )
            }
        )
    }
}
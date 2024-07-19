package com.cp.brittany.dixon.ui.screens.home.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.utills.composecalendar.SelectableCalendarSample
import com.cp.brittany.dixon.utills.sheets.BottomSheet
import com.cp.brittany.dixon.utills.sheets.BottomSheetState

@Composable
fun DateCalendarBottomSheet(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
    screenType: String,
    selectDate: (String) -> Unit,
    startEndDate: (String, String) -> Unit,
    slotsList: MutableList<WorkoutData> = ArrayList(),
) {
    BottomSheet(
        state = state,
        modifier = modifier,
        skipPeeked = true,
    ) {
        SelectableCalendarSample(
            state = state,
            screenType = screenType,
            selectDate = selectDate,
            startEndDate = startEndDate,
            slotsList = slotsList
        )
    }
}
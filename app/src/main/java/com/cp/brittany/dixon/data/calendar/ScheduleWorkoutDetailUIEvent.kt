package com.cp.brittany.dixon.data.calendar

import com.cp.brittany.dixon.model.calendar.Images

sealed class ScheduleWorkoutDetailUIEvent {
    data class AddFavouriteChanged(val imageList: MutableList<Images>?,val id:Int) : ScheduleWorkoutDetailUIEvent()
}

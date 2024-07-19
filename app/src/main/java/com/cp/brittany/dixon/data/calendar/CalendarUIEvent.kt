package com.cp.brittany.dixon.data.calendar

sealed class CalendarUIEvent {
    data class SelectDateFromWeekCalendar(val date: String) :
        CalendarUIEvent()

    data class GetImages(val images: List<String>,val workoutId:Int) :
        CalendarUIEvent()

    object RefreshData : CalendarUIEvent()

}

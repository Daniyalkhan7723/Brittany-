package com.cp.brittany.dixon.data.calendar

data class CalendarUIState(
    var date: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var startDateForWeekCalendar: String = "",
    var endDateForWeekCalendar: String = "",
    var workoutId: Int = 0,
    var getImages: List<String>? = null,
)

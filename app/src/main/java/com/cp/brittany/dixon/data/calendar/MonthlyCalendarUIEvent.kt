package com.cp.brittany.dixon.data.calendar

sealed class MonthlyCalendarUIEvent {

    data class SelectDateFromWeekCalendar(val date: String) : MonthlyCalendarUIEvent()
    object RefreshData : MonthlyCalendarUIEvent()

    data class SelectStartEndDateFromCalendar(val startDate: String, val eneDate: String) :
        MonthlyCalendarUIEvent()

    data class GetSlotsForWeekCalendar(val startDate: String, val eneDate: String) :
        MonthlyCalendarUIEvent()

}

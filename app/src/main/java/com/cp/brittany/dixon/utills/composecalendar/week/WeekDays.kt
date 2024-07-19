package com.cp.brittany.dixon.utills.composecalendar.week

import androidx.compose.runtime.Immutable
import com.cp.brittany.dixon.utills.composecalendar.day.Day

@Immutable
data class WeekDays(
    val isFirstWeekOfTheMonth: Boolean = false,
    val days: List<Day>,
)

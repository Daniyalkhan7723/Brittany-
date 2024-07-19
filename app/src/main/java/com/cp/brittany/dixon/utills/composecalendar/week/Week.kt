package com.cp.brittany.dixon.utills.composecalendar.week

import com.cp.brittany.dixon.utills.composecalendar.selection.fillUpTo
import java.time.LocalDate

data class Week(
    val days: List<LocalDate>,
) {

    init {
        require(days.size == DaysInAWeek)
    }

    internal constructor(firstDay: LocalDate) : this(
        listOf(firstDay).fillUpTo(firstDay.plusDays((DaysInAWeek - 1).toLong()))
    )

    val start: LocalDate get() = days.first()

    val end: LocalDate get() = days.last()

    operator fun inc(): Week = plusWeeks(1)

    operator fun dec(): Week = plusWeeks(-1)

    private fun plusWeeks(value: Long): Week = copy(days = days.map { it.plusWeeks(value) })


}

package com.cp.brittany.dixon.utills.composecalendar.day

import java.time.LocalDate

/**
 * Container for basic info about the displayed day
 *
 * @param date local date of the day
 * @param isCurrentDay whenever the day is the today's date
 * @param isFromCurrentMonth whenever the day is from currently rendered month
 */
interface Day {
    val date: LocalDate
    val isCurrentDay: Boolean
    val isFromCurrentMonth: Boolean
}

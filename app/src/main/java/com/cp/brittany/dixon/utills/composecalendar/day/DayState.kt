package com.cp.brittany.dixon.utills.composecalendar.day

import androidx.compose.runtime.Stable
import com.cp.brittany.dixon.utills.composecalendar.selection.SelectionState

/**
 * Contains information about current selection as well as date of rendered day
 */
@Stable
data class DayState<T : SelectionState>(
    private val day: Day,
    val selectionState: T,
) : Day by day

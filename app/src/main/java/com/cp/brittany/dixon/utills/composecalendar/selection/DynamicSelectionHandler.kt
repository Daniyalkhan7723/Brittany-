package com.cp.brittany.dixon.utills.composecalendar.selection

import com.cp.brittany.dixon.utills.composecalendar.util.addOrRemoveIfExists
import java.time.LocalDate

/**
 * Helper class for calculating new selection, when using a [DynamicSelectionState] approach.
 * @param date clicked on date
 * @param selection current selection
 * @param selectionMode current selection mode
 * @returns new selection in a form of a list of local dates.
 */
object DynamicSelectionHandler {
    fun calculateNewSelection(
        date: LocalDate,
        selection: List<LocalDate>,
        selectionMode: SelectionMode,
    ): List<LocalDate> = when (selectionMode) {
        SelectionMode.None -> emptyList()
        SelectionMode.Single -> if (date == selection.firstOrNull()) {
            emptyList()
        } else {
            listOf(date)
        }

        SelectionMode.Multiple -> selection.addOrRemoveIfExists(date)
        SelectionMode.Period -> when {
            date.isBefore(selection.startOrMax()) -> listOf(date)
            date.isAfter(selection.startOrMax()) -> selection.fillUpTo(date)
            date == selection.startOrMax() -> emptyList()
            else -> selection
        }
    }
}

package com.cp.brittany.dixon.utills.composecalendar.header

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import com.cp.brittany.dixon.utills.composecalendar.week.Week
import java.time.LocalDate

// Factory function
fun WeekState(initialWeek: Week): WeekState = WeekStateImpl(initialWeek)

@Stable
interface WeekState {
    var currentWeek: Week

    companion object {
        fun Saver(): Saver<WeekState, String> = Saver(
            save = { it.currentWeek.start.toString() },
            restore = { WeekState(initialWeek = Week(firstDay = LocalDate.parse(it))) }
        )
    }
}

@Stable
private class WeekStateImpl(
    initialWeek: Week,
) : WeekState {
    private var _currentWeek by mutableStateOf<Week>(initialWeek)
    override var currentWeek: Week
        get() = _currentWeek
        set(value) {
            _currentWeek = value
        }
}

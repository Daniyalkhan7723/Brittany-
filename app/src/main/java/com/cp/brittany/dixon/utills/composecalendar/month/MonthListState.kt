package com.cp.brittany.dixon.utills.composecalendar.month

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.composecalendar.header.MonthState
import com.cp.brittany.dixon.utills.composecalendar.util.throttleOnOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Stable
internal class MonthListState(
    private val coroutineScope: CoroutineScope,
    private val initialMonth: YearMonth,
    private val monthState: MonthState,
    private val listState: LazyListState,
    private val screenType: String
) {

    private val currentFirstVisibleMonth by derivedStateOf {
        getMonthForPage(listState.firstVisibleItemIndex)
    }

    //This will  reset value of selected state
    init {
        snapshotFlow { monthState.currentMonth }.onEach { month ->
            moveToMonth(month)
        }.launchIn(coroutineScope)

        with(listState) {
            snapshotFlow { currentFirstVisibleMonth }
                .throttleOnOffset()
                .onEach { newMonth ->
                    monthState.currentMonth = newMonth
                }.launchIn(coroutineScope)
        }
    }

    fun getMonthForPage(index: Int): YearMonth =
        if (screenType == Constants.SCHEDULE_WORKOUTS) initialMonth.plusMonths((index - SecondStartIndex).toLong()) else initialMonth.plusMonths(
            (index - StartIndex).toLong()
        )

    private fun moveToMonth(month: YearMonth) {
        if (month == currentFirstVisibleMonth) return
        initialMonth.minus(month).let { offset ->
            coroutineScope.launch {
                if (screenType == Constants.SCHEDULE_WORKOUTS) {
                    listState.animateScrollToItem((SecondStartIndex - offset).toInt())
                } else {
                    listState.animateScrollToItem((StartIndex - offset).toInt())
                }

            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MonthListState

        if (monthState != other.monthState) return false
        if (listState != other.listState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = monthState.hashCode()
        result = 31 * result + listState.hashCode()
        return result
    }
}

private operator fun YearMonth.minus(other: YearMonth) =
    ChronoUnit.MONTHS.between(other, this)


internal const val PagerItemCount = 20_000
internal const val SecondStartIndex = PagerItemCount / 2

internal const val StartIndex = 0

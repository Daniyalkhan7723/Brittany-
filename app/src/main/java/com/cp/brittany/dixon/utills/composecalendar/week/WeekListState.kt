package com.cp.brittany.dixon.utills.composecalendar.week

import java.time.temporal.ChronoUnit


private operator fun Week.minus(other: Week) =
    ChronoUnit.WEEKS.between(other.start, this.start)

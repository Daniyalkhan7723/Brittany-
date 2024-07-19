package com.cp.brittany.dixon.model.insight

import com.cp.brittany.dixon.model.workout.Pagination

data class AllInsightModel(
    val data: ArrayList<AllInsight>? = null,
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val pagination: Pagination,

    )
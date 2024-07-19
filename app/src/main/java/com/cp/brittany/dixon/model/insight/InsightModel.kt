package com.cp.brittany.dixon.model.insight

import androidx.compose.runtime.Immutable

@Immutable
data class InsightModel(
    val data: InsightData? = null,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

@Immutable
data class InsightData(
    val allInsight: List<AllInsight>? = null,
    val latestInsights: List<AllInsight>? = null
)

@Immutable
data class AllInsight(
    val category: String? = null,
    val category_id: Int? = null,
    val created_at: Any? = null,
    val description: String? = null,
    val short_description: String? = null,
    val duration: String? = null,
    val id: Int? = null,
    val is_favourited: Boolean? = null,
    val is_liked: Boolean? = null,
    val likes: Int? = null,
    val status: Int? = null,
    val thumbnail: String? = null,
    val title: String? = null
)
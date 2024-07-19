package com.cp.brittany.dixon.model.insight

data class InsightLikeModel(
    val data: AllInsight? = null,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

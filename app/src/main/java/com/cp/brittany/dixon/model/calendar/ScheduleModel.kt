package com.cp.brittany.dixon.model.calendar

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
data class ScheduleModel(
    val data: ScheduleData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

@Immutable
data class ScheduleData(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: Category,
    val category_id: Int,
    val is_featured: Boolean,
    val is_premium: Boolean,
    val status: Int,
    val views: Int,
    val created_at: String,
    val deleted_at: Any,
    val updated_at: String,
    val video: ScheduleVideo,
    val images: ArrayList<Images>,

    )

@Immutable
data class Category(
    val id: Int,
    val name: String
)

@Immutable
data class ScheduleVideo(
    val created_at: String? = null,
    val duration: String? = null,
    val id: Int? = null,
    val is_premium: Int? = null,
    val status: Int? = null,
    val thumbnail: String? = null,
    val updated_at: String? = null,
    val url: String? = null,
    val views: Int? = null,
    val workout_id: Int? = null
)


@Immutable
@Parcelize
data class Images(
    val id: Int? = null,
    val user_id: Int? = null,
    val is_premium: Int? = null,
    val status: Int? = null,
    val image: String? = null,
    val updated_at: String? = null,
    val created_at: String? = null,
    val workout_id: Int? = null
) : Parcelable
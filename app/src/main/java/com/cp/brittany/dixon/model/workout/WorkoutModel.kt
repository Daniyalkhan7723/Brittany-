package com.cp.brittany.dixon.model.workout

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.cp.brittany.dixon.model.calendar.Images
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Immutable
data class WorkoutModel(
    val data: List<Data>? = null,
    val message: String? = null,
    val pagination: Any? = null,
    val status: Boolean? = null,
    val statusCode: Int? = null
)

@Immutable
@Parcelize
data class Data(
    val id: Int? = null,
    val name: String? = null,
    val workouts: List<WorkoutData>? = null,
) : Parcelable

@Immutable
@Parcelize
data class WorkoutData(
    val category_id: Int? = null,
    val description: String? = null,
    val id: Int? = null,
    val progress: Int? = null,
    val published_time: String? = null,
    val title: String? = null,
    val video: Video? = null,
    val is_favorite: Boolean? = null,
    val category: String? = null,
    val is_featured: Boolean? = null,
    val status: Int? = null,
    val date: String? = null,
    val images: MutableList<Images>? = null,
    val watched_time: String? = null
) : Parcelable

@Immutable
@Parcelize
data class Video(
    val duration: String? = null,
    val id: Int? = null,
    val thumbnail: String? = null,
    val url: String? = null
) : Parcelable
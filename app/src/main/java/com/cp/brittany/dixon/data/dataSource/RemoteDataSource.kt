package com.cp.brittany.dixon.data.dataSource

import androidx.paging.PagingData
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.model.calendar.DeleteImageResponse
import com.cp.brittany.dixon.model.calendar.ScheduleModel
import com.cp.brittany.dixon.model.favourite.AddFavourite
import com.cp.brittany.dixon.model.favourite.GetInsideFavourites
import com.cp.brittany.dixon.model.favourite.GetWorkoutFavourites
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.insight.InsightLikeModel
import com.cp.brittany.dixon.model.insight.InsightModel
import com.cp.brittany.dixon.model.subscription.GetPackagesResponse
import com.cp.brittany.dixon.model.workout.StartWorkoutResponse
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.model.workout.WorkoutDetailsResponse
import com.cp.brittany.dixon.model.workout.WorkoutModel
import com.cp.brittany.dixon.ui.viewModel.workout.FeatureWorkoutModel
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface RemoteDataSource {
    suspend fun login(body: JsonObject): Response<LoginResponse>
    suspend fun socialLogin(body: JsonObject): Response<LoginResponse>
    suspend fun signUp(body: JsonObject): Response<LoginResponse>
    suspend fun verifyOtp(body: JsonObject): Response<LoginResponse>
    suspend fun sendOtp(body: JsonObject): Response<LoginResponse>
    suspend fun resetPassword(body: JsonObject): Response<LoginResponse>
    suspend fun verifyEmail(body: JsonObject): Response<LoginResponse>
    suspend fun logout(authToken: String, body: JsonObject): Response<LoginResponse>
    suspend fun editProfile(authToken: String, body: JsonObject): Response<LoginResponse>
    suspend fun resetPassword(authToken: String, body: JsonObject): Response<LoginResponse>
    suspend fun getProfile(authToken: String): Response<LoginResponse>
    suspend fun uploadAvatar(authToken: String, file: MultipartBody.Part?): Response<LoginResponse>
    suspend fun featureWorkout(authToken: String): Response<FeatureWorkoutModel>
    suspend fun workoutDetail(authToken: String, body: JsonObject): Response<WorkoutDetailsResponse>
    suspend fun categoryBaseWorkOut(authToken: String): Response<WorkoutModel>
    suspend fun workoutFavouriteList(authToken: String): Response<GetWorkoutFavourites>
    suspend fun insideFavouriteList(authToken: String): Response<GetInsideFavourites>
    suspend fun categoryById(authToken: String, body: JsonObject): Response<WorkoutModel>
    suspend fun addFavourite(authToken: String, body: JsonObject): Response<AddFavourite>
    suspend fun addSchedule(authToken: String, body: JsonObject): Response<ScheduleModel>
    suspend fun uploadImages(
        authToken: String,
        workoutId: RequestBody,
        pictures: List<MultipartBody.Part>
    ): Response<ScheduleModel>

    suspend fun getScheduleSlots(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel>

    suspend fun getByDatesWorkouts(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel>

//    fun getSeeMoreWorkout(
//        bearerToken: String,
//        id: String,
//        searchItem: String
//    ): Flow<PagingData<WorkoutData>>

    fun allWorkout(
        bearerToken: String,
        searchItem: String,
        id: String,
        screenType: String
    ): Flow<PagingData<WorkoutData>>

    fun allInsights(
        bearerToken: String, searchItem: String, screenType: String
    ): Flow<PagingData<AllInsight>>

    suspend fun workoutByCategory(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel>

    suspend fun randomWorkoutByCategory(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel>

    suspend fun deleteImage(authToken: String, body: JsonObject): Response<DeleteImageResponse>
    suspend fun insightData(authToken: String): Response<InsightModel>
    suspend fun addInsightFavourite(authToken: String, body: JsonObject): Response<InsightLikeModel>
    suspend fun likeInsight(authToken: String, body: JsonObject): Response<InsightLikeModel>
    suspend fun getAllPackages(authToken: String): Response<GetPackagesResponse>

    suspend fun startWorkout(authToken: String, body: JsonObject): Response<StartWorkoutResponse>
    suspend fun purchase(authToken: String, body: JsonObject): Response<LoginResponse>


}
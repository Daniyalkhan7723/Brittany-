package com.cp.brittany.dixon.data.api

import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.model.calendar.DeleteImageResponse
import com.cp.brittany.dixon.model.calendar.ScheduleModel
import com.cp.brittany.dixon.model.favourite.AddFavourite
import com.cp.brittany.dixon.model.favourite.GetInsideFavourites
import com.cp.brittany.dixon.model.favourite.GetWorkoutFavourites
import com.cp.brittany.dixon.model.insight.AllInsightModel
import com.cp.brittany.dixon.model.insight.InsightLikeModel
import com.cp.brittany.dixon.model.insight.InsightModel
import com.cp.brittany.dixon.model.subscription.GetPackagesResponse
import com.cp.brittany.dixon.model.workout.StartWorkoutResponse
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutDetailsResponse
import com.cp.brittany.dixon.model.workout.WorkoutModel
import com.cp.brittany.dixon.ui.viewModel.workout.FeatureWorkoutModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {
    @POST("login")
    suspend fun login(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("social-login")
    suspend fun socialLogin(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verifyOtp(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("send-otp")
    suspend fun sendOtp(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("verify-email")
    suspend fun verifyEmail(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @POST("reset-password")
    suspend fun resetPassword(
        @Body body: JsonObject
    ): Response<LoginResponse>

    @GET("logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @POST("users/update-profile")
    suspend fun editProfile(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("users/upload-avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") token: String, @Part attachment: MultipartBody.Part?
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @POST("users/change-password")
    suspend fun resetPassword(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("workouts/category-base")
    suspend fun categoryBase(
        @Header("Authorization") token: String
    ): Response<WorkoutModel>

    @Headers("Accept: application/json")
    @GET("workouts/favourite-list")
    suspend fun workoutFavouriteList(
        @Header("Authorization") token: String
    ): Response<GetWorkoutFavourites>

    @Headers("Accept: application/json")
    @GET("insights/favourit-list")
    suspend fun insideFavouriteList(
        @Header("Authorization") token: String
    ): Response<GetInsideFavourites>


    @Headers("Accept: application/json")
    @GET("workouts/detail")
    suspend fun categoryById(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutModel>

    @Headers("Accept: application/json")
    @POST("workouts/by-category")
    suspend fun workoutByCategory(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>

    @Headers("Accept: application/json")
    @POST("workouts/random-by-category")
    suspend fun randomWorkoutByCategory(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>

    @Headers("Accept: application/json")
    @POST("workouts/all")
    suspend fun workoutAll(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>


    @Headers("Accept: application/json")
    @POST("workouts/all")
    suspend fun allWorkouts(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>

    @Headers("Accept: application/json")
    @GET("workouts/feature")
    suspend fun featureWorkout(
        @Header("Authorization") token: String
    ): Response<FeatureWorkoutModel>

    @Headers("Accept: application/json")
    @POST("workouts/detail")
    suspend fun workoutDetails(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutDetailsResponse>

    @Headers("Accept: application/json")
    @POST("workouts/favourite")
    suspend fun addFavourite(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<AddFavourite>

    @Headers("Accept: application/json")
    @POST("workouts/schedule")
    suspend fun addSchedule(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<ScheduleModel>

    @Headers("Accept: application/json")
    @POST("workouts/get-by-date")
    suspend fun getByDatesWorkouts(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>

    @Headers("Accept: application/json")
    @POST("workouts/date-list")
    suspend fun getScheduleSlots(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<WorkoutByCategoryModel>

    @Headers("Accept: application/json")
    @POST("workouts/delete-image")
    suspend fun deleteImage(
        @Header("Authorization") token: String, @Body body: JsonObject
    ): Response<DeleteImageResponse>


    @Multipart
    @Headers("Accept: application/json")
    @POST("workouts/upload-image")
    suspend fun uploadImages(
        @Header("Authorization") token: String,
        @Part("workout_id") workoutId: RequestBody?,
        @Part images: List<MultipartBody.Part>,
    ): Response<ScheduleModel>

    @Headers("Accept: application/json")
    @GET("insights/limited-list?limit=10")
    suspend fun insightData(
        @Header("Authorization") token: String,
//        @Body body: JsonObject
    ): Response<InsightModel>

    @Headers("Accept: application/json")
    @GET("insights/all")
    suspend fun allInsights(
        @Header("Authorization") token: String,
        @Query("search") search: String,
        @Query("page") page: Int
    ): Response<AllInsightModel>

    @Headers("Accept: application/json")
    @GET("insights/all-favourit-list")
    suspend fun allFavouriteInsights(
        @Header("Authorization") token: String,
        @Query("search") search: String,
        @Query("page") page: Int
    ): Response<AllInsightModel>

    @Headers("Accept: application/json")
    @GET("workouts/all-favourite-list")
    suspend fun allFavouriteWorkouts(
        @Header("Authorization") token: String,
        @Query("search") search: String,
        @Query("page") page: Int
    ): Response<WorkoutByCategoryModel>


    @Headers("Accept: application/json")
    @POST("insights/favourit")
    suspend fun addInsightFavourite(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Response<InsightLikeModel>

    @Headers("Accept: application/json")
    @POST("insights/like")
    suspend fun likeInsight(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Response<InsightLikeModel>

    @Headers("Accept: application/json")
    @GET("packages/all")
    suspend fun getAllPackages(
        @Header("Authorization") token: String,
    ): Response<GetPackagesResponse>

    @Headers("Accept: application/json")
    @POST("workouts/start")
    suspend fun startWorkout(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Response<StartWorkoutResponse>

    @Headers("Accept: application/json")
    @POST("packages/purchase")
    suspend fun purchase(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Response<LoginResponse>

}
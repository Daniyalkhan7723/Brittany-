package com.cp.brittany.dixon.data.dataSource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cp.brittany.dixon.data.api.ApiInterface
import com.cp.brittany.dixon.data.api.PagingSource
import com.cp.brittany.dixon.data.api.PagingSourceForInsights
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
import javax.inject.Inject

class RemoteDataSourceIml @Inject constructor(
    private val apiService: ApiInterface,
) : RemoteDataSource {
    override suspend fun login(body: JsonObject): Response<LoginResponse> {
        return apiService.login(body)
    }

    override suspend fun socialLogin(body: JsonObject): Response<LoginResponse> {
        return apiService.socialLogin(body)
    }

    override suspend fun signUp(body: JsonObject): Response<LoginResponse> {
        return apiService.register(body)
    }

    override suspend fun verifyOtp(body: JsonObject): Response<LoginResponse> {
        return apiService.verifyOtp(body)
    }

    override suspend fun sendOtp(body: JsonObject): Response<LoginResponse> {
        return apiService.sendOtp(body)
    }

    override suspend fun verifyEmail(body: JsonObject): Response<LoginResponse> {
        return apiService.verifyEmail(body)
    }

    override suspend fun resetPassword(body: JsonObject): Response<LoginResponse> {
        return apiService.resetPassword(body)
    }

    override suspend fun logout(authToken: String, body: JsonObject): Response<LoginResponse> {
        return apiService.logout(authToken)
    }

    override suspend fun editProfile(authToken: String, body: JsonObject): Response<LoginResponse> {
        return apiService.editProfile(
            authToken,
            body
        )
    }

    override suspend fun getProfile(authToken: String): Response<LoginResponse> {
        return apiService.getProfile(authToken)
    }

    override suspend fun uploadAvatar(
        authToken: String,
        file: MultipartBody.Part?
    ): Response<LoginResponse> {
        return apiService.uploadAvatar(authToken, file)
    }

    override suspend fun resetPassword(
        authToken: String,
        body: JsonObject
    ): Response<LoginResponse> {
        return apiService.resetPassword(authToken, body)
    }

    override suspend fun featureWorkout(authToken: String): Response<FeatureWorkoutModel> {
        return apiService.featureWorkout(authToken)
    }

    override suspend fun workoutDetail(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutDetailsResponse> {
        return apiService.workoutDetails(authToken, body)
    }

    override suspend fun categoryBaseWorkOut(authToken: String): Response<WorkoutModel> {
        return apiService.categoryBase(authToken)
    }

    override suspend fun workoutFavouriteList(authToken: String): Response<GetWorkoutFavourites> {
        return apiService.workoutFavouriteList(authToken)
    }

    override suspend fun insideFavouriteList(authToken: String): Response<GetInsideFavourites> {
        return apiService.insideFavouriteList(authToken)
    }

    override suspend fun categoryById(authToken: String, body: JsonObject): Response<WorkoutModel> {
        return apiService.categoryById(authToken, body)
    }

    override suspend fun workoutByCategory(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel> {
        return apiService.workoutByCategory(authToken, body)
    }

    override suspend fun randomWorkoutByCategory(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel> {
        return apiService.randomWorkoutByCategory(authToken, body)
    }

    override suspend fun addFavourite(authToken: String, body: JsonObject): Response<AddFavourite> {
        return apiService.addFavourite(authToken, body)
    }

    override suspend fun addSchedule(
        authToken: String,
        body: JsonObject
    ): Response<ScheduleModel> {
        return apiService.addSchedule(authToken, body)
    }

    override suspend fun getScheduleSlots(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel> {
        return apiService.getScheduleSlots(authToken, body)
    }

    override suspend fun getByDatesWorkouts(
        authToken: String,
        body: JsonObject
    ): Response<WorkoutByCategoryModel> {
        return apiService.getByDatesWorkouts(authToken, body)
    }

//    override fun getSeeMoreWorkout(
//        bearerToken: String,
//        id: String,
//        searchItem: String
//    ): Flow<PagingData<WorkoutData>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//            ),
//            pagingSourceFactory = {
//                PagingSource(
//                    apiInterface = apiService,
//                    bearerToken = bearerToken,
//                    categoryId = id,
//                    query = searchItem,
//                    type = Constants.SCREEN_ALL_WORKOUTS
//                )
//            }
//        ).flow
//    }

    override fun allWorkout(
        bearerToken: String,
        searchItem: String,
        id: String,
        screenType: String,
    ): Flow<PagingData<WorkoutData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                PagingSource(
                    apiInterface = apiService,
                    bearerToken = bearerToken,
                    categoryId = id,
                    query = searchItem,
                    screenType = screenType
                )
            }
        ).flow
    }

    override fun allInsights(
        bearerToken: String,
        searchItem: String,
        screenType: String
    ): Flow<PagingData<AllInsight>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                PagingSourceForInsights(
                    apiInterface = apiService,
                    bearerToken = bearerToken,
                    query = searchItem,
                    screenType = screenType
                )
            }
        ).flow
    }


    override suspend fun uploadImages(
        authToken: String,
        workoutId: RequestBody,
        pictures: List<MultipartBody.Part>
    ): Response<ScheduleModel> {
        return apiService.uploadImages(authToken, workoutId, pictures)
    }

    override suspend fun deleteImage(
        authToken: String,
        body: JsonObject
    ): Response<DeleteImageResponse> {
        return apiService.deleteImage(authToken, body)
    }

    override suspend fun insightData(
        authToken: String,
    ): Response<InsightModel> {
        return apiService.insightData(authToken)
    }

    override suspend fun addInsightFavourite(
        authToken: String,
        body: JsonObject
    ): Response<InsightLikeModel> {
        return apiService.addInsightFavourite(authToken, body)
    }

    override suspend fun likeInsight(
        authToken: String,
        body: JsonObject
    ): Response<InsightLikeModel> {
        return apiService.likeInsight(authToken, body)

    }

    override suspend fun getAllPackages(authToken: String): Response<GetPackagesResponse> {
        return apiService.getAllPackages(authToken)
    }

    override suspend fun startWorkout(
        authToken: String,
        body: JsonObject
    ): Response<StartWorkoutResponse> {
        return apiService.startWorkout(authToken, body)
    }

    override suspend fun purchase(
        authToken: String,
        body: JsonObject
    ): Response<LoginResponse> {
        return apiService.purchase(authToken, body)
    }

}
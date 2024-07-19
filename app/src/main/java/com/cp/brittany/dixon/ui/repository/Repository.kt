package com.cp.brittany.dixon.ui.repository

import android.content.Context
import androidx.paging.PagingData
import com.cp.brittany.dixon.data.dataSource.RemoteDataSource
import com.cp.brittany.dixon.data.api.BaseApiResponse
import com.cp.brittany.dixon.utills.NetworkResult
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
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val preferenceHelper: SharePreferenceHelper,
    @ApplicationContext private val mContext: Context
) : BaseApiResponse() {

    suspend fun login(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.login(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun socialLogin(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.socialLogin(body) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun signUp(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.signUp(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyOtp(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.verifyOtp(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyEmail(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.verifyEmail(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendOtp(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.sendOtp(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resendPassword(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.resetPassword(body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun logout(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.logout("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editProfile(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.editProfile("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetPassword(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.resetPassword("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfile(
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.getProfile("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadAvatar(
        file: MultipartBody.Part?
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.uploadAvatar("Bearer $token", file) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun categoryBaseWorkout(
    ): Flow<NetworkResult<WorkoutModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.categoryBaseWorkOut("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun workoutFavouriteList(
    ): Flow<NetworkResult<GetWorkoutFavourites>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.workoutFavouriteList("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insideFavouriteList(
    ): Flow<NetworkResult<GetInsideFavourites>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.insideFavouriteList("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun featureWorkout(
    ): Flow<NetworkResult<FeatureWorkoutModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.featureWorkout("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun workoutDetail(
        body: JsonObject
    ): Flow<NetworkResult<WorkoutDetailsResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.workoutDetail("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun workoutByCategory(
        body: JsonObject
    ): Flow<NetworkResult<WorkoutByCategoryModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.workoutByCategory("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun randomWorkoutByCategory(
        body: JsonObject
    ): Flow<NetworkResult<WorkoutByCategoryModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.randomWorkoutByCategory("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }


//    fun workoutByCategory(
//        token: String,
//        categoryId: String,
//        searchItem: String
//    ): Flow<PagingData<WorkoutData>> {
//        return remoteDataSource.getSeeMoreWorkout(token, categoryId, searchItem)
//    }

    fun allWorkouts(
        token: String,
        categoryId: String = "",
        searchItem: String,
        screenType: String
    ): Flow<PagingData<WorkoutData>> {
        return remoteDataSource.allWorkout(token, searchItem, categoryId, screenType)
    }

    fun allInsights(
        token: String,
        searchItem: String,
        screenType: String
    ): Flow<PagingData<AllInsight>> {
        return remoteDataSource.allInsights(token, searchItem, screenType = screenType)
    }

    suspend fun addFavourite(
        body: JsonObject
    ): Flow<NetworkResult<AddFavourite>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.addFavourite("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addSchedule(
        body: JsonObject
    ): Flow<NetworkResult<ScheduleModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.addSchedule("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadImages(
        workoutId: RequestBody,
        pictures: List<MultipartBody.Part>
    ): Flow<NetworkResult<ScheduleModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall {
                remoteDataSource.uploadImages(
                    "Bearer $token",
                    workoutId,
                    pictures
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getScheduleSlots(
        body: JsonObject
    ): Flow<NetworkResult<WorkoutByCategoryModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.getScheduleSlots("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getByDatesWorkouts(
        body: JsonObject
    ): Flow<NetworkResult<WorkoutByCategoryModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.getByDatesWorkouts("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteImage(
        body: JsonObject
    ): Flow<NetworkResult<DeleteImageResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.deleteImage("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insightData(
        body: JsonObject
    ): Flow<NetworkResult<InsightModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.insightData("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun addInsightFavourite(
        body: JsonObject
    ): Flow<NetworkResult<InsightLikeModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.addInsightFavourite("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun likeInsight(
        body: JsonObject
    ): Flow<NetworkResult<InsightLikeModel>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.likeInsight("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllPackages(
    ): Flow<NetworkResult<GetPackagesResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.getAllPackages("Bearer $token") })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun startWorkout(
        body: JsonObject
    ): Flow<NetworkResult<StartWorkoutResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.startWorkout("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun purchase(
        body: JsonObject
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            val token = preferenceHelper.getToken()
            emit(safeApiCall { remoteDataSource.purchase("Bearer $token", body) })
        }.flowOn(Dispatchers.IO)
    }


}
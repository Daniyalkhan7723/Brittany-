package com.cp.brittany.dixon.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.billingclient.api.ProductDetails
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.shared.ImagesSharedModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.ChooseSubscription
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
) : AndroidViewModel(applicationContext) {

    private val _shareWorkoutDataToDetailScreenResponse: MutableStateFlow<WorkoutData?> =
        MutableStateFlow(null)
    val shareWorkoutDataToDetailScreenResponse: MutableStateFlow<WorkoutData?> =
        _shareWorkoutDataToDetailScreenResponse

    private val _allInsightResponse: MutableStateFlow<AllInsight?> = MutableStateFlow(null)
    val allInsightResponse: MutableStateFlow<AllInsight?> = _allInsightResponse

    private val _packageDetailResponse: MutableStateFlow<ProductDetails?> = MutableStateFlow(null)
    val packageDetailResponse: MutableStateFlow<ProductDetails?> = _packageDetailResponse

    private val _chooseSubscriptionObject: MutableStateFlow<ChooseSubscription?> =
        MutableStateFlow(null)
    val chooseSubscriptionObject: MutableStateFlow<ChooseSubscription?> = _chooseSubscriptionObject

    private val _refreshWorkoutListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshWorkoutListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        _refreshWorkoutListAfterLikeOrDislikeResponse

    private val _refreshInsightListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshInsightListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        _refreshInsightListAfterLikeOrDislikeResponse

    private val _refreshFavouriteWorkoutListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshFavouriteWorkoutListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        _refreshFavouriteWorkoutListAfterLikeOrDislikeResponse

    private val _refreshFavouriteInsightListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshFavouriteInsightListAfterLikeOrDislikeResponse: MutableStateFlow<Boolean> =
        _refreshFavouriteInsightListAfterLikeOrDislikeResponse


    private val _refreshScheduleWorkoutsResponse: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshScheduleWorkoutsResponse: MutableStateFlow<Boolean> =
        _refreshScheduleWorkoutsResponse


    private val _refreshWorkoutDetails: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshWorkoutDetails: MutableStateFlow<Boolean> =
        _refreshWorkoutDetails

    private val _refreshWorkoutImagesWhenDeleteSingleImage: MutableStateFlow<ImagesSharedModel> =
        MutableStateFlow(ImagesSharedModel(false, 0))
    val refreshWorkoutImagesWhenDeleteSingleImage: MutableStateFlow<ImagesSharedModel> =
        _refreshWorkoutImagesWhenDeleteSingleImage

    private val _navigateBackFromVideoPlayerScreen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val navigateBackFromVideoPlayerScreen: MutableStateFlow<Boolean> =
        _navigateBackFromVideoPlayerScreen


    private val _refreshDetailScreen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val refreshDetailScreen: MutableStateFlow<Boolean> =
        _refreshDetailScreen


    fun shareWorkoutDataToDetailScreen(data: WorkoutData?) {
        _shareWorkoutDataToDetailScreenResponse.value = data
    }

    fun refreshScheduleWorkout(check: Boolean) {
        _refreshScheduleWorkoutsResponse.value = check
    }

    fun refreshWorkoutDetails(check: Boolean) {
        _refreshWorkoutDetails.value = check
    }

    fun refreshWorkoutImagesWhenSingleImageIsDelete(imagesSharedModel: ImagesSharedModel) {
        _refreshWorkoutImagesWhenDeleteSingleImage.value = imagesSharedModel
    }

    fun refreshDetailScreen(value: Boolean) {
        _refreshDetailScreen.value = value
    }

    fun setWorkoutByDate(data: WorkoutData?) {
        _shareWorkoutDataToDetailScreenResponse.value = data
    }

    fun sendInsightDataToDetailScreen(data: AllInsight?) {
        _allInsightResponse.value = data
    }

    fun sendPackageDataToPackageDetail(data: ProductDetails?) {
        _packageDetailResponse.value = data
    }

    fun sendChooseSubscriptionObject(data: ChooseSubscription?) {
        _chooseSubscriptionObject.value = data
    }

    fun refreshWorkoutListAfterLikeOrDislike(check: Boolean) {
        _refreshWorkoutListAfterLikeOrDislikeResponse.value = check
    }

    fun refreshInsightListAfterLikeOrDislike(check: Boolean) {
        _refreshInsightListAfterLikeOrDislikeResponse.value = check
    }

    fun refreshFavouriteWorkoutListAfterLikeOrDislike(check: Boolean) {
        _refreshFavouriteWorkoutListAfterLikeOrDislikeResponse.value = check
    }

    fun refreshFavouriteInsightListAfterLikeOrDislike(check: Boolean) {
        _refreshFavouriteInsightListAfterLikeOrDislikeResponse.value = check
    }


    fun setBackNavigationFromPlayer(check: Boolean) {
        _navigateBackFromVideoPlayerScreen.value = check
    }


    fun resetResponse() {
        _refreshScheduleWorkoutsResponse.value = false
        _navigateBackFromVideoPlayerScreen.value = false
        _refreshWorkoutImagesWhenDeleteSingleImage.value =
            ImagesSharedModel(deleteOrNot = false, imageId = 0)
    }

    fun resetResponseForRefreshDetailScreen() {
        _refreshDetailScreen.value = false
    }


    fun resetResponseForWorkoutFavourites() {
        _refreshFavouriteWorkoutListAfterLikeOrDislikeResponse.value = false
    }


    fun resetResponseForInsightFavourites() {
        _refreshFavouriteInsightListAfterLikeOrDislikeResponse.value = false
    }

    fun resetsShareWorkoutDataToDetailScreenResponse() {
        _shareWorkoutDataToDetailScreenResponse.value = null
    }


    fun resetResponseForRefreshWorkoutListAfterLikeOrDislikeResponse() {
        _refreshWorkoutListAfterLikeOrDislikeResponse.value = false
    }

    fun resetResponseForRefreshInsightListAfterLikeOrDislikeResponse() {
        _refreshInsightListAfterLikeOrDislikeResponse.value = false
    }


    fun resetRefreshDetails() {
        _refreshWorkoutDetails.value = false
    }

}
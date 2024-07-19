package com.cp.brittany.dixon.ui.viewModel.profile

import android.app.Application
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface

import android.os.Environment
import android.webkit.URLUtil
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.editProfile.EditProfileEvent
import com.cp.brittany.dixon.data.editProfile.EditProfileUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.compressMyImage
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor
    (
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,

    ) : ViewModel() {
    private val _editProfileResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val editProfileResponse: StateFlow<NetworkResult<LoginResponse>> = _editProfileResponse


    private val _addImageResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addImageResponse: StateFlow<NetworkResult<LoginResponse>> =
        _addImageResponse.asStateFlow()

    var editProfileUIState = mutableStateOf(EditProfileUIState())

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        editProfileUIState.value.fullName = preference.getUser()?.name ?: ""
    }

    private fun editProfile() = viewModelScope.launch(Dispatchers.IO) {
        if (validateDataWithRules()) {
            if (applicationContext.isNetworkAvailable()) {
                val fullName = editProfileUIState.value.fullName
                _editProfileResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject()
                jsonObject.addProperty("name", fullName)
                repository.editProfile(jsonObject).collect { response ->
                    response.apply {
                        _editProfileResponse.value = response
                        if (response.data?.status == true) {
                            response.data.data.let {
                                preference.saveUser(it)
                            }
                        }
                    }
                }
            } else {
                _editProfileResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }

    }

    private fun addUserImage() {
        if (applicationContext.isNetworkAvailable()) {
            val image = editProfileUIState.value.image
            viewModelScope.launch {
                _addImageResponse.value = NetworkResult.Loading()
                val multipartBody: MultipartBody.Part? = if (!URLUtil.isValidUrl(image)) {
                    val file = File(image)
                    val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData(
                        "avatar", file.name, requestFile
                    )
                } else
                    null

//                val multipartBody: MultipartBody.Part? = if (!URLUtil.isValidUrl(image)) {
//                    val file = if (Utils.isNeedToCompressImage(image)) {
//                        val bitmap = BitmapFactory.decodeFile(image)
//                        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                        val imageFileName = "JPEG_" + timeStamp + "_"
//                        val sd_main = File(Environment.getExternalStorageDirectory(), "/Pictures")
//                        val newFile = bitmap.compressMyImage(sd_main, imageFileName)
//                        val oldExif = ExifInterface(image)
//                        val exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)
//                        if (exifOrientation != null) {
//                            val newExif = ExifInterface(newFile?.path ?: "")
//                            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
//                            newExif.saveAttributes()
//                        }
//                        newFile
//                    } else {
//                        File(image)
//                    }
//                    Utils.fileSize(file)
//                    if (file != null) {
//                        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
//                        MultipartBody.Part.createFormData(
//                            "avatar", file.name, requestFile
//                        )
//                    } else {
//                        null
//                    }
//                } else null


                repository.uploadAvatar(multipartBody).collect { values ->
                    if (values.data?.status == true) {
                        val user = preference.getUser()
                        if (user != null) {
                            values.data.data.let { userImage ->
                                user.avatar = values.data.data.avatar
                                preference.saveUser(user)
                            }
                        }
                    }
                    _addImageResponse.value = values
                }
            }
        } else {
            _addImageResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }


    private fun validateDataWithRules(): Boolean {
        if (editProfileUIState.value.fullName.trim().isEmpty()) {
            editProfileUIState.value = editProfileUIState.value.copy(
                fullNameErrorMessage = applicationContext.resources.getString(R.string.error_full_name)
            )
            editProfileUIState.value = editProfileUIState.value.copy(
                fullNameError = true
            )
            return false
        }
        return true
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.NameChanged -> {
                editProfileUIState.value = editProfileUIState.value.copy(
                    fullName = event.name
                )

                editProfileUIState.value = editProfileUIState.value.copy(
                    fullNameError = false
                )
            }

            EditProfileEvent.EditProfileButtonClicked -> {
                editProfile()
            }

            is EditProfileEvent.GetImageButtonClicked -> {
                editProfileUIState.value = editProfileUIState.value.copy(
                    image = event.image ?: ""
                )
                addUserImage()
            }
        }
    }

    fun resetResponse() {
        _editProfileResponse.value = NetworkResult.NoCallYet()
        _addImageResponse.value = NetworkResult.NoCallYet()
    }


    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }


}



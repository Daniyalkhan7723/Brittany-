package com.cp.brittany.dixon.ui.viewModel.profile

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.model.auth.User
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
) : ViewModel() {
    private val _getProfile: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getProfile: StateFlow<NetworkResult<LoginResponse>> = _getProfile
    var getProfileResponseData = mutableStateOf(User())

    init {
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _getProfile.value = NetworkResult.Loading()
            repository.getProfile().collect { response ->
                response.apply {
                    _getProfile.value = response
                    if (response.data?.status == true) {
                        response.data.data.let {
                            preference.saveUser(it)
                        }
                    }
                }
            }
        } else {
            _getProfile.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    fun resetResponse() {
        _getProfile.value = NetworkResult.NoCallYet()
    }


}



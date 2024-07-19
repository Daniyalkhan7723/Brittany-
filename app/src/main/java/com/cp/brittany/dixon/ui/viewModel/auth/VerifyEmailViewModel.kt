package com.cp.brittany.dixon.ui.viewModel.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.verifyEmail.VerifyEmailUIEvent
import com.cp.brittany.dixon.data.verifyEmail.VerifyEmailUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor
    (
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
    private val stateHandle: SavedStateHandle

) : ViewModel() {
    var verifyEmailUIState = mutableStateOf(VerifyEmailUIState())

    private val _verifyEmailResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val verifyEmailResponse: StateFlow<NetworkResult<LoginResponse>> = _verifyEmailResponse

    private val _sendOtpResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.Loading())
    val sendOtpResponse: StateFlow<NetworkResult<LoginResponse>> = _sendOtpResponse

    init {
        val type = stateHandle.get<String>(Constants.SCREEN_TYPE)
        if (type == Constants.FROM_LOGIN) {
            sendOtp()
        }
    }

    private fun verifyEmail() = viewModelScope.launch(Dispatchers.IO) {
        if (validateLoginUIDataWithRules()) {
            val code = verifyEmailUIState.value.otpCode
            val email = stateHandle.get<String>(Constants.EMAIL)
            val type = stateHandle.get<String>(Constants.SCREEN_TYPE)
            if (applicationContext.isNetworkAvailable()) {
                _verifyEmailResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    addProperty("email", email)
                    addProperty("code", code.trim())
                }
                repository.verifyEmail(jsonObject).collect { response ->
                    response.apply {
                        if (data != null && data.status && data.data.is_verified_email == true) {
                            preference.apply {
                                saveUser(data.data)
                                saveToken(data.data.token ?: "")
                                if (type == Constants.FROM_SIGN_UP) {
                                    saveIsFromSignUp(true)
                                }
                                saveUserLogIn()
                            }
                        }
                        _verifyEmailResponse.value = response
                    }
                }

            } else {
                _verifyEmailResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }


    }

    private fun sendOtp() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val email = stateHandle.get<String>(Constants.EMAIL)
            val jsonObject = JsonObject().apply {
                addProperty("email", email?.trim())
            }
            repository.sendOtp(jsonObject).collect() { response ->
                _sendOtpResponse.value = response
            }

        } else {
            _sendOtpResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }

    fun onEvent(event: VerifyEmailUIEvent) {
        when (event) {
            is VerifyEmailUIEvent.OtpCodeChanged -> {
                verifyEmailUIState.value = verifyEmailUIState.value.copy(
                    otpCode = event.otpCode
                )
                verifyEmailUIState.value = verifyEmailUIState.value.copy(
                    otpCodeError = false
                )
            }

            is VerifyEmailUIEvent.VerifyButtonClicked -> {
                verifyEmail()
            }
        }
    }

    private fun validateLoginUIDataWithRules(): Boolean {
        if (verifyEmailUIState.value.otpCode.trim().isEmpty()) {
            verifyEmailUIState.value = verifyEmailUIState.value.copy(
                otpCodeErrorMessage = applicationContext.resources.getString(R.string.error_otp)
            )
            verifyEmailUIState.value = verifyEmailUIState.value.copy(
                otpCodeError = true
            )
            return false
        }
        return true
    }

    fun resetResponse() {
        _verifyEmailResponse.value = NetworkResult.NoCallYet()
        _sendOtpResponse.value = NetworkResult.NoCallYet()

    }


}



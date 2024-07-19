package com.cp.brittany.dixon.ui.viewModel.auth.forgotPassword

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.forgotPassword.confirmYourEmail.ConfirmYourEmailUIEvent
import com.cp.brittany.dixon.data.forgotPassword.confirmYourEmail.ConfirmYourEmailUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.Constants.Companion.EMAIL
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
class ConfirmYourEmailViewModel @Inject constructor
    (
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    var confirmYourEmailUIState = mutableStateOf(ConfirmYourEmailUIState())

    private val _verifyEmailResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val verifyEmailResponse: StateFlow<NetworkResult<LoginResponse>> = _verifyEmailResponse

    private fun verifyEmail() = viewModelScope.launch(Dispatchers.IO) {
        if (validateLoginUIDataWithRules()) {
            val code = confirmYourEmailUIState.value.otpCode
            if (applicationContext.isNetworkAvailable()) {
                _verifyEmailResponse.value = NetworkResult.Loading()
                val email = stateHandle.get<String>(EMAIL)
                val jsonObject = JsonObject().apply {
                    addProperty("email", email)
                    addProperty("code", code.trim())
                }
                repository.verifyOtp(jsonObject).collect() { response ->
                    _verifyEmailResponse.value = response
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

    fun onEvent(event: ConfirmYourEmailUIEvent) {
        when (event) {
            is ConfirmYourEmailUIEvent.OtpCodeChanged -> {
                confirmYourEmailUIState.value = confirmYourEmailUIState.value.copy(
                    otpCode = event.otpCode
                )
                confirmYourEmailUIState.value = confirmYourEmailUIState.value.copy(
                    otpCodeError = false
                )
            }


            is ConfirmYourEmailUIEvent.NextButtonClicked -> {
                verifyEmail()
            }
        }
    }


    private fun validateLoginUIDataWithRules(): Boolean {
        if (confirmYourEmailUIState.value.otpCode.trim().isEmpty()) {
            confirmYourEmailUIState.value = confirmYourEmailUIState.value.copy(
                otpCodeErrorMessage = applicationContext.resources.getString(R.string.error_otp)
            )
            confirmYourEmailUIState.value = confirmYourEmailUIState.value.copy(
                otpCodeError = true
            )
            return false
        }
        return true
    }

    fun resetResponse() {
        _verifyEmailResponse.value = NetworkResult.NoCallYet()
    }


}



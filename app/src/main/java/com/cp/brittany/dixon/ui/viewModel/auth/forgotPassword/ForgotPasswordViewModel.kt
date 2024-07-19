package com.cp.brittany.dixon.ui.viewModel.auth.forgotPassword

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.forgotPassword.addEmail.ForgotPasswordUIEvent
import com.cp.brittany.dixon.data.forgotPassword.addEmail.ForgotPasswordUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.cp.brittany.dixon.utills.isValidEmail
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application
) : ViewModel() {

    var forgotPasswordUIState = mutableStateOf(ForgotPasswordUIState())

    private val _forgotPasswordResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val forgotPasswordResponse: StateFlow<NetworkResult<LoginResponse>> = _forgotPasswordResponse

    private fun verifyEmail() = viewModelScope.launch(Dispatchers.IO) {
        if (validateLoginUIDataWithRules()) {
            val email = forgotPasswordUIState.value.email
            if (applicationContext.isNetworkAvailable()) {
                _forgotPasswordResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    addProperty("email", email.trim())
                }
                repository.sendOtp(jsonObject).collect() { response ->
                    _forgotPasswordResponse.value = response
                }

            } else {
                _forgotPasswordResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }


    }

    fun onEvent(event: ForgotPasswordUIEvent) {
        when (event) {
            is ForgotPasswordUIEvent.EmailChanged -> {
                forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                    email = event.email
                )
                forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                    emailError = false
                )
            }

            is ForgotPasswordUIEvent.NextButtonClicked -> {
                verifyEmail()
            }

        }
    }

    private fun validateLoginUIDataWithRules(): Boolean {
        if (forgotPasswordUIState.value.email.trim().isEmpty()) {
            forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_email)
            )
            forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                emailError = true
            )
            return false
        } else if (!forgotPasswordUIState.value.email.trim().isValidEmail()) {
            forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_invalid_email)
            )
            forgotPasswordUIState.value = forgotPasswordUIState.value.copy(
                emailError = true
            )
            return false
        }
        return true
    }

    fun resetResponse() {
        _forgotPasswordResponse.value = NetworkResult.NoCallYet()
    }


}



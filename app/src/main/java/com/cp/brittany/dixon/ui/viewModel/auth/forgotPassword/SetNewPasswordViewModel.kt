package com.cp.brittany.dixon.ui.viewModel.auth.forgotPassword

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.forgotPassword.setNewPassword.SetNewPasswordUIEvent
import com.cp.brittany.dixon.data.forgotPassword.setNewPassword.SetNewPasswordUIState
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
class SetNewPasswordViewModel @Inject constructor(
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    var setUpNewPasswordUIState = mutableStateOf(SetNewPasswordUIState())

    private val _setUpNewPasswordResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val setUpNewPasswordResponse: StateFlow<NetworkResult<LoginResponse>> =
        _setUpNewPasswordResponse

    private fun verifyEmail() = viewModelScope.launch(Dispatchers.IO) {
        if (validateLoginUIDataWithRules()) {
            val password = setUpNewPasswordUIState.value.password
            val confirmPassword = setUpNewPasswordUIState.value.password
            if (applicationContext.isNetworkAvailable()) {
                _setUpNewPasswordResponse.value = NetworkResult.Loading()
                val email = stateHandle.get<String>(Constants.EMAIL)
                val jsonObject = JsonObject().apply {
                    addProperty("email", email)
                    addProperty("password", password)
                    addProperty("password_confirmation", confirmPassword)
                }
                repository.resendPassword(jsonObject).collect { response ->
                    _setUpNewPasswordResponse.value = response
                }

            } else {
                _setUpNewPasswordResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }


    }

    fun onEvent(event: SetNewPasswordUIEvent) {
        when (event) {

            is SetNewPasswordUIEvent.PasswordChanged -> {
                setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                    password = event.password
                )

                setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                    passwordError = false
                )

            }

            is SetNewPasswordUIEvent.ConfirmPasswordChanged -> {
                setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                    confirmPassword = event.confirmPassword
                )
                setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                    confirmPasswordError = false
                )
            }

            is SetNewPasswordUIEvent.NextButtonClicked -> {
                verifyEmail()
            }

        }
    }

    private fun validateLoginUIDataWithRules(): Boolean {
        if (setUpNewPasswordUIState.value.password.trim().isEmpty()) {
            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                passwordErrorMessage = applicationContext.resources.getString(R.string.error_password)
            )

            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                passwordError = true
            )

            return false
        } else if (setUpNewPasswordUIState.value.confirmPassword.trim().isEmpty()) {
            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                confirmPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_password)
            )

            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                confirmPasswordError = true
            )

            return false
        }
        if (setUpNewPasswordUIState.value.confirmPassword.trim() != setUpNewPasswordUIState.value.password.trim()
        ) {
            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                confirmPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_password_match)
            )
            setUpNewPasswordUIState.value = setUpNewPasswordUIState.value.copy(
                confirmPasswordError = true
            )

            return false
        }
        return true
    }

    fun resetResponse() {
        _setUpNewPasswordResponse.value = NetworkResult.NoCallYet()
    }


}



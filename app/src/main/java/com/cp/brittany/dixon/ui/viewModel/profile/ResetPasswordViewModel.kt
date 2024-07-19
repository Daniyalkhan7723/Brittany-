package com.cp.brittany.dixon.ui.viewModel.profile

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.resetPassword.ResetPasswordUIEvent
import com.cp.brittany.dixon.data.resetPassword.ResetPasswordUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
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
class ResetPasswordViewModel @Inject constructor(
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
) : AndroidViewModel(applicationContext) {

    var resetPasswordUIState = mutableStateOf(ResetPasswordUIState())

    private val _resetPasswordResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val resetPasswordResponse: StateFlow<NetworkResult<LoginResponse>> = _resetPasswordResponse

    private fun resetPassword() = viewModelScope.launch(Dispatchers.IO) {
        if (validateDataWithRules()) {
            val oldPassword = resetPasswordUIState.value.oldPassword
            val newPassword = resetPasswordUIState.value.newPassword
            val confirmNewPassword = resetPasswordUIState.value.confirmNewPassword
            if (applicationContext.isNetworkAvailable()) {
                _resetPasswordResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    addProperty("old_password", oldPassword)
                    addProperty("password", newPassword)
                    addProperty("password_confirmation", confirmNewPassword)
                }
                repository.resetPassword(jsonObject).collect() { response ->
                    _resetPasswordResponse.value = response
                }
            } else {
                _resetPasswordResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }

    }


    fun onEvent(event: ResetPasswordUIEvent) {
        when (event) {
            is ResetPasswordUIEvent.OldPasswordChanged -> {
                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    oldPassword = event.oldPassword
                )

                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    oldPasswordError = false
                )

            }

            is ResetPasswordUIEvent.NewPasswordChanged -> {
                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    newPassword = event.newPassword
                )

                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    newPasswordError = false
                )

            }

            is ResetPasswordUIEvent.ConfirmNewPasswordChanged -> {
                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    confirmNewPassword = event.confirmNewPassword
                )
                resetPasswordUIState.value = resetPasswordUIState.value.copy(
                    confirmNewPasswordError = false
                )
            }

            is ResetPasswordUIEvent.NextButtonClicked -> {
                resetPassword()
            }

        }
    }

    private fun validateDataWithRules(): Boolean {
        if (resetPasswordUIState.value.oldPassword.trim().isEmpty()) {
            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                oldPasswordErrorMessage = applicationContext.resources.getString(R.string.error_old_password)
            )

            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                oldPasswordError = true
            )

            return false
        } else if (resetPasswordUIState.value.newPassword.trim().isEmpty()) {
            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                newPasswordErrorMessage = applicationContext.resources.getString(R.string.error_new_password)
            )

            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                newPasswordError = true
            )

            return false
        } else if (resetPasswordUIState.value.confirmNewPassword.trim().isEmpty()) {
            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                confirmNewPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_new_password)
            )

            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                confirmNewPasswordError = true
            )

            return false
        }
        if (resetPasswordUIState.value.confirmNewPassword.trim() != resetPasswordUIState.value.confirmNewPassword.trim()) {
            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                confirmNewPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_new_password_match)
            )
            resetPasswordUIState.value = resetPasswordUIState.value.copy(
                confirmNewPasswordError = true
            )

            return false
        }
        return true

    }


    fun resetResponse() {
        _resetPasswordResponse.value = NetworkResult.NoCallYet()
    }


}

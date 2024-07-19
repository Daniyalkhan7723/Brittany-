package com.cp.brittany.dixon.ui.viewModel.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.login.LoginUIEvent
import com.cp.brittany.dixon.data.login.LoginUIState
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.getMyDeviceId
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.cp.brittany.dixon.utills.isValidEmail
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor
    (
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
    private val stateHandle: SavedStateHandle,
    var googleSignInClient: GoogleSignInClient,
    var getLoginManager: LoginManager


) : ViewModel() {
    var loginUIState = mutableStateOf(LoginUIState())

    private val _loginResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val response: StateFlow<NetworkResult<LoginResponse>> = _loginResponse


    fun login() = viewModelScope.launch(Dispatchers.IO) {
        if (validateLoginUIDataWithRules()) {
            val email = loginUIState.value.email
            val password = loginUIState.value.password

            if (applicationContext.isNetworkAvailable()) {
                _loginResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    addProperty("email", email.trim())
                    addProperty("password", password)
                    addProperty("device_id", applicationContext.getMyDeviceId())
                    addProperty("fcm_token", "fcn_asdfasdfasfdasdf")
                    addProperty("timezone", TimeZone.getDefault().id)
                }
                repository.login(jsonObject).collect { response ->
                    response.apply {
                        if (data != null && data.status && data.data.is_verified_email == true) {
                            preference.apply {
                                saveUser(data.data)
                                saveToken(data.data.token ?: "")
                                saveUserLogIn()
                                saveIsFromSignUp(true)
                            }
                        }
                        _loginResponse.value = response
                    }

                }

            } else {
                _loginResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }

    }

    private fun socialLogin() = viewModelScope.launch(Dispatchers.IO) {
        val accountDetails = loginUIState.value.googleAccountDetails
        val loginType = loginUIState.value.loginType
        if (applicationContext.isNetworkAvailable()) {
            _loginResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject().apply {
                accountDetails?.apply {
                    addProperty("email", email?.trim())
                    addProperty("name", displayName)
                    addProperty("token", id)
                    addProperty("device_id", applicationContext.getMyDeviceId())
                    addProperty("fcm_token", "fcn_asdfasdfasfdasdf")
                    addProperty("timezone", TimeZone.getDefault().id)
                    addProperty("type", loginType)
                }
            }
            repository.socialLogin(jsonObject).collect { response ->
                response.apply {
                    if (data != null && data.status && data.data.is_verified_email == true) {
                        preference.apply {
                            saveUser(data.data)
                            saveToken(data.data.token ?: "")
                            saveUserLogIn()
                        }
                    }
                    _loginResponse.value = response
                }
                _loginResponse.value = response
            }

        } else {
            _loginResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                loginUIState.value = loginUIState.value.copy(
                    emailError = false
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                loginUIState.value = loginUIState.value.copy(
                    passwordError = false
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }

            is LoginUIEvent.GoogleSignInButton -> {
                loginUIState.value = loginUIState.value.copy(
                    googleAccountDetails = event.googleAccountDetails
                )

                loginUIState.value = loginUIState.value.copy(
                    loginType = event.loginType
                )
                socialLogin()
            }
        }
    }

    private fun validateLoginUIDataWithRules(): Boolean {
        if (loginUIState.value.email.trim().isEmpty()) {
            loginUIState.value = loginUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_email)
            )
            loginUIState.value = loginUIState.value.copy(
                emailError = true
            )
            return false
        } else if (!loginUIState.value.email.trim().isValidEmail()) {

            loginUIState.value = loginUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_invalid_email)
            )

            loginUIState.value = loginUIState.value.copy(
                emailError = true
            )
            return false
        } else if (loginUIState.value.password.trim().isEmpty()) {

            loginUIState.value = loginUIState.value.copy(
                passwordErrorMessage = applicationContext.resources.getString(R.string.error_password)
            )

            loginUIState.value = loginUIState.value.copy(
                passwordError = true
            )

            return false
        }
        return true
    }

    fun resetResponse() {
        _loginResponse.value = NetworkResult.NoCallYet()
    }


}



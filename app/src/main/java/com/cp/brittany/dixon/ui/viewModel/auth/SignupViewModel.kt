package com.cp.brittany.dixon.ui.viewModel.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.signup.SignUpUIState
import com.cp.brittany.dixon.data.signup.SignupUIEvent
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.auth.LoginResponse
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
class SignupViewModel @Inject constructor(
    private val repository: Repository,
    private val preference: SharePreferenceHelper,
    private val applicationContext: Application,
    var googleSignInClient: GoogleSignInClient,
    var getLoginManager: LoginManager


) : AndroidViewModel(applicationContext) {
    var signUpUIState = mutableStateOf(SignUpUIState())

    private val _signupResponse: MutableStateFlow<NetworkResult<LoginResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val signupResponse: StateFlow<NetworkResult<LoginResponse>> = _signupResponse

    private fun signUp() = viewModelScope.launch(Dispatchers.IO) {
        if (validateDataWithRules()) {
            val fullName = signUpUIState.value.fullName
            val email = signUpUIState.value.email
            val password = signUpUIState.value.password
            val confirmPassword = signUpUIState.value.confirmPassword
            if (applicationContext.isNetworkAvailable()) {
                _signupResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    addProperty("name", fullName.trim())
                    addProperty("email", email.trim())
                    addProperty("password", password)
                    addProperty("password_confirmation", confirmPassword)
                    addProperty("device_id", applicationContext.getMyDeviceId())
                    addProperty("fcm_token", "fcn_asdfasdfasfdasdf")
                    addProperty("timezone", TimeZone.getDefault().id)
                }
                repository.signUp(jsonObject).collect() { response ->
                    _signupResponse.value = response
                }

            } else {
                _signupResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }

    }

    private fun socialLogin() = viewModelScope.launch(Dispatchers.IO) {
        if (validateDataWithRules()) {
            val accountDetails = signUpUIState.value.googleAccountDetails
            val loginType = signUpUIState.value.loginType

            if (applicationContext.isNetworkAvailable()) {
                _signupResponse.value = NetworkResult.Loading()
                val jsonObject = JsonObject().apply {
                    accountDetails?.apply {
                        addProperty("email", email?.trim())
                        addProperty("name", displayName)
                        addProperty("token", idToken)
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
                        _signupResponse.value = response
                    }

                }

            } else {
                _signupResponse.value = NetworkResult.NoInternet(
                    applicationContext.resources.getString(
                        R.string.no_internet
                    )
                )
            }
        }

    }


    fun onEvent(event: SignupUIEvent) {
        when (event) {
            is SignupUIEvent.NameChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    fullName = event.name
                )

                signUpUIState.value = signUpUIState.value.copy(
                    fullNameError = false
                )
            }


            is SignupUIEvent.EmailChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    email = event.email
                )

                signUpUIState.value = signUpUIState.value.copy(
                    emailError = false
                )

            }


            is SignupUIEvent.PasswordChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    password = event.password
                )

                signUpUIState.value = signUpUIState.value.copy(
                    passwordError = false
                )

            }

            is SignupUIEvent.ConfirmPasswordChanged -> {
                signUpUIState.value = signUpUIState.value.copy(
                    confirmPassword = event.confirmPassword
                )
                signUpUIState.value = signUpUIState.value.copy(
                    confirmPasswordError = false
                )
            }

            is SignupUIEvent.RegisterButtonClicked -> {
                signUp()
            }

            is SignupUIEvent.GoogleSignInButton -> {
                signUpUIState.value = signUpUIState.value.copy(
                    googleAccountDetails = event.googleAccountDetails
                )

                signUpUIState.value = signUpUIState.value.copy(
                    loginType = event.loginType
                )

                socialLogin()
            }
        }
    }

    private fun validateDataWithRules(): Boolean {
        if (signUpUIState.value.fullName.trim().isEmpty()) {
            signUpUIState.value = signUpUIState.value.copy(
                fullNameErrorMessage = applicationContext.resources.getString(R.string.error_full_name)
            )
            signUpUIState.value = signUpUIState.value.copy(
                fullNameError = true
            )
            return false
        } else if (signUpUIState.value.email.trim().isEmpty()) {
            signUpUIState.value = signUpUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_email)
            )
            signUpUIState.value = signUpUIState.value.copy(
                emailError = true
            )
            return false
        } else if (!signUpUIState.value.email.trim().isValidEmail()) {
            signUpUIState.value = signUpUIState.value.copy(
                emailErrorMessage = applicationContext.resources.getString(R.string.error_invalid_email)
            )

            signUpUIState.value = signUpUIState.value.copy(
                emailError = true
            )
            return false
        } else if (signUpUIState.value.password.trim().isEmpty()) {
            signUpUIState.value = signUpUIState.value.copy(
                passwordErrorMessage = applicationContext.resources.getString(R.string.error_password)
            )

            signUpUIState.value = signUpUIState.value.copy(
                passwordError = true
            )

            return false
        } else if (signUpUIState.value.confirmPassword.trim().isEmpty()) {
            signUpUIState.value = signUpUIState.value.copy(
                confirmPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_password)
            )

            signUpUIState.value = signUpUIState.value.copy(
                confirmPasswordError = true
            )

            return false
        } else if (signUpUIState.value.confirmPassword.trim() != signUpUIState.value.password.trim()
        ) {
            signUpUIState.value = signUpUIState.value.copy(
                confirmPasswordErrorMessage = applicationContext.resources.getString(R.string.error_confirm_password_match)
            )
            signUpUIState.value = signUpUIState.value.copy(
                confirmPasswordError = true
            )

            return false
        }
        return true

    }


    fun resetResponse() {
        _signupResponse.value = NetworkResult.NoCallYet()
    }


}

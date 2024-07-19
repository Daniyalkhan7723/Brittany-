package com.cp.brittany.dixon.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import com.cp.brittany.dixon.ui.viewModel.auth.LoginViewModel
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.AuthHeaderComponent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.ClickableLoginTextComponent
import com.cp.brittany.dixon.ui.components.DividerTextComponent
import com.cp.brittany.dixon.ui.components.TextFieldComponent
import com.cp.brittany.dixon.ui.components.PasswordTextFieldComponent
import com.cp.brittany.dixon.ui.components.SocialMediaSection
import com.cp.brittany.dixon.ui.components.UnderLinedTextComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.data.login.LoginUIEvent
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.navigation.navGraphs.authGraph.AuthRoute
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Constants.Companion.GO_HOME
import com.cp.brittany.dixon.utills.Constants.Companion.GO_VERIFY
import com.cp.brittany.dixon.utills.sdp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject


@Composable
inline fun LoginScreen(
    navController: NavController? = null,
    crossinline moveNextScreen: (String, String) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val response by loginViewModel.response.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
    var isButtonEnable by remember { mutableStateOf(true) }
    var isSocialLoginLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val alpha = remember {
        Animatable(0f)
    }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }

    val onTextChangedEmail = remember<(String) -> Unit> {
        {
            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
        }
    }

    val onTextChangedPassword = remember<(String) -> Unit> {
        {
            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
        }
    }


    when (response) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (response.data?.status == true) {
                if (response.data?.data?.is_verified_email == true) {
                    showToast(
                        title = response.data?.message ?: "", isSuccess = true
                    )
                    LaunchedEffect(key1 = "") {
                        // will be canceled and re-launched if sth is changed
                        coroutineScope.launch {
                            delay(1500)
                            moveNextScreen(GO_HOME, "")
                        }
                    }

                } else {
                    moveNextScreen(GO_VERIFY, loginViewModel.loginUIState.value.email)
                }
            } else {
                isButtonEnable = true
                showToast(
                    title = response.data?.message ?: "", isSuccess = false
                )
            }

            loginViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            isLoading = false
            isButtonEnable = true
            loginViewModel.resetResponse()
            val message: String = try {
                val jObjError = JSONObject(response.message.toString())
                jObjError.get("message").toString()
            } catch (e: Exception) {
                response.message ?: context.resources.getString(
                    R.string.something_went_wrong
                )
            }

            showToast(
                title = message, isSuccess = false
            )
        }

        is NetworkResult.Loading<*> -> {
            isLoading = true
            isButtonEnable = false
            loginViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            isButtonEnable = true
            loginViewModel.resetResponse()

            showToast(
                title = response.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    LaunchedEffect(key1 = Unit) {
        alpha.animateTo(
            1f, animationSpec = tween(2000)
        )

    }

    LaunchedEffect(key1 = Unit) {
        offsetY.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = 1000, easing = LinearEasing
            )
        )
    }

    Column( // or whatever your parent composable is
        modifier = Modifier.verticalScroll(state = scrollState)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(12.sdp)
                .alpha(alpha.value)
                .offset {
                    IntOffset(
                        offsetX.value.toInt(), offsetY.value.toInt()
                    )
                })
            {
                AuthHeaderComponent(
                    title = stringResource(id = R.string.login_to_your_account),
                    subtitle = stringResource(id = R.string.please_login_to_continue),
                    logo = R.drawable.logo_glampian,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.sdp)
                )

                TextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    onTextChanged = onTextChangedEmail,
                    errorStatus = loginViewModel.loginUIState.value.emailError,
                    errorText = loginViewModel.loginUIState.value.emailErrorMessage,
                    stringValue = loginViewModel.loginUIState.value.email,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                    errorModifier = Modifier.padding(start = 4.sdp, top = 5.sdp)
                )

                Spacer(modifier = Modifier.height(20.sdp))

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    onTextSelected = onTextChangedPassword,
                    errorStatus = loginViewModel.loginUIState.value.passwordError,
                    errorText = loginViewModel.loginUIState.value.passwordErrorMessage,
                    stringValue = loginViewModel.loginUIState.value.password,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                    keyboardActions = ImeAction.Done
                )

                Spacer(modifier = Modifier.height(30.sdp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    UnderLinedTextComponent(value = stringResource(id = R.string.forgot_password),
                        onButtonClicked = {
                            navController?.navigate(AuthRoute.ForgotPasswordBaseScreen.route)
                        })
                }

                Spacer(modifier = Modifier.height(13.sdp))

                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    modifier = Modifier
//                        .bounceClick()
                        .fillMaxWidth()
                        .heightIn(45.sdp),
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.background,
                    isLoading = isLoading,
                    isEnabled = isButtonEnable
                )

                Spacer(modifier = Modifier.height(10.sdp))

                DividerTextComponent()

                Spacer(modifier = Modifier.height(10.sdp))

                SocialMediaSection(loginViewModel.googleSignInClient,
                    loginViewModel.getLoginManager,
                    onGoogleSignInCompleted = { accountDetails ->
                        loginViewModel.onEvent(
                            LoginUIEvent.GoogleSignInButton(
                                accountDetails, Constants.SOCIAL_LOGIN_TYPE_GOOGLE
                            )
                        )
                    },
                    onGoogleSignInError = {

                    },
                    loadingForSocial = {
                        isSocialLoginLoading = it
                    })

                Spacer(modifier = Modifier.height(100.sdp))
                ClickableLoginTextComponent(
                    tryingToLogin = false,
                    onTextSelected = {
                        navController?.navigate(AuthRoute.SignUpScreen.route) {
                            popUpTo(AuthRoute.GetStartedScreen.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.sdp)
                )

            }

            if (isSocialLoginLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.sdp),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
}
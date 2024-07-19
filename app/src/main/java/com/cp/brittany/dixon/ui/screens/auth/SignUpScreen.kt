package com.cp.brittany.dixon.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cp.brittany.dixon.ui.viewModel.auth.SignupViewModel
import com.cp.brittany.dixon.data.signup.SignupUIEvent
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.AuthHeaderComponent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.ClickableLoginTextComponent
import com.cp.brittany.dixon.ui.components.DividerTextComponent
import com.cp.brittany.dixon.ui.components.TextFieldComponent
import com.cp.brittany.dixon.ui.components.PasswordTextFieldComponent
import com.cp.brittany.dixon.ui.components.SocialMediaSection
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.navigation.navGraphs.authGraph.AuthRoute
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import org.json.JSONObject


@Composable
inline fun SignUpScreen(
    navController: NavController? = null,
    moveVerifyEmailScreen: (String) -> Unit,
    signupViewModel: SignupViewModel = hiltViewModel()

) {
    val scrollState = rememberScrollState()
    val response by signupViewModel.signupResponse.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    var isSocialLoginLoading by remember { mutableStateOf(false) }
    var isButtonEnable by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val alpha = remember {
        Animatable(0f)
    }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }

    when (response) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (response.data?.status == true) {
                showToast(
                    title = response.data?.message ?: "", isSuccess = true
                )
                moveVerifyEmailScreen(signupViewModel.signUpUIState.value.email)
            } else {
                isButtonEnable = true
                showToast(
                    title = response.data?.message ?: "", isSuccess = false
                )
            }
            signupViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            isButtonEnable = true
            isLoading = false
            signupViewModel.resetResponse()
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
            isButtonEnable = false
            isLoading = true
            signupViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isButtonEnable = false
            isLoading = false
            signupViewModel.resetResponse()

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
                }) {
                AuthHeaderComponent(
                    stringResource(id = R.string.signup_to_your_account),
                    stringResource(id = R.string.please_register_to_continue),
                    R.drawable.logo_glampian,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.sdp)
                )

                TextFieldComponent(
                    labelValue = stringResource(id = R.string.name),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.NameChanged(it))
                    },
                    errorStatus = signupViewModel.signUpUIState.value.fullNameError,
                    errorText = signupViewModel.signUpUIState.value.fullNameErrorMessage,
                    stringValue = signupViewModel.signUpUIState.value.fullName,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                    errorModifier = Modifier.padding(start = 4.sdp, top = 5.sdp)


                )

                Spacer(modifier = Modifier.height(15.sdp))

                TextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    onTextChanged = {
                        signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                    },
                    errorStatus = signupViewModel.signUpUIState.value.emailError,
                    errorText = signupViewModel.signUpUIState.value.emailErrorMessage,
                    stringValue = signupViewModel.signUpUIState.value.email,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                    errorModifier = Modifier.padding(start = 4.sdp, top = 5.sdp)

                )

                Spacer(modifier = Modifier.height(15.sdp))

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                    },
                    errorStatus = signupViewModel.signUpUIState.value.passwordError,
                    errorText = signupViewModel.signUpUIState.value.passwordErrorMessage,
                    stringValue = signupViewModel.signUpUIState.value.password,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                )
                Spacer(modifier = Modifier.height(15.sdp))

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.confirm_password),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.ConfirmPasswordChanged(it))
                    },
                    errorStatus = signupViewModel.signUpUIState.value.confirmPasswordError,
                    errorText = signupViewModel.signUpUIState.value.confirmPasswordErrorMessage,
                    stringValue = signupViewModel.signUpUIState.value.confirmPassword,
                    unFocusColor = MaterialTheme.colorScheme.onTertiary,
                    focusColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 2.sdp),
                    keyboardActions = ImeAction.Done
                )

                Spacer(modifier = Modifier.height(20.sdp))

                ButtonComponent(
                    value = stringResource(id = R.string.register),
                    onButtonClicked = {
                        signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
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

                Spacer(modifier = Modifier.height(20.sdp))

                DividerTextComponent()

                Spacer(modifier = Modifier.height(10.sdp))

                SocialMediaSection(signupViewModel.googleSignInClient,
                    signupViewModel.getLoginManager,

                    onGoogleSignInCompleted = { accountDetails ->
                        signupViewModel.onEvent(
                            SignupUIEvent.GoogleSignInButton(
                                accountDetails, Constants.SOCIAL_LOGIN_TYPE_GOOGLE
                            )
                        )
                    },
                    onGoogleSignInError = {

                    },
                    loadingForSocial = {
                        isSocialLoginLoading = it
                    })
                Spacer(modifier = Modifier.height(30.sdp))

                ClickableLoginTextComponent(
                    tryingToLogin = true, onTextSelected = {
                        navController?.navigate(AuthRoute.LoginScreen.route) {
                            popUpTo(AuthRoute.GetStartedScreen.route) {
                                saveState = false
                            }
                            launchSingleTop = false
                            restoreState = false
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





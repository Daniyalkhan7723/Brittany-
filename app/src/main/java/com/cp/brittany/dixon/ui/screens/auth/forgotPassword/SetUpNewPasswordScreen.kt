package com.cp.brittany.dixon.ui.screens.auth.forgotPassword

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.DefaultBackArrow
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.PasswordTextFieldComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.data.forgotPassword.setNewPassword.SetNewPasswordUIEvent
import com.cp.brittany.dixon.ui.viewModel.auth.forgotPassword.SetNewPasswordViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import org.json.JSONObject

@Composable
inline fun SetUpNewPasswordScreen(
    navController: NavController? = null,
    crossinline onBackPress: () -> Unit,
    crossinline dismissListener: () -> Unit,
    setNewPasswordViewModel: SetNewPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var showBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val alpha = remember {
        Animatable(0f)
    }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }
    var isLoading by remember { mutableStateOf(false) }
    var isButtonEnable by remember { mutableStateOf(true) }

    val response by setNewPasswordViewModel.setUpNewPasswordResponse.collectAsStateWithLifecycle()

    BackHandler(true) {
        onBackPress()
        navController?.popBackStack()
    }

    LaunchedEffect(key1 = Unit) {
        alpha.animateTo(
            1f,
            animationSpec = tween(1000)
        )

    }

    LaunchedEffect(key1 = Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }
    BrittanyDixonTheme {
        when (response) {
            is NetworkResult.Success<*> -> {
                isLoading = false
                if (response.data?.status == true) {
                    showBottomSheet.value = true
                    setNewPasswordViewModel.resetResponse()
                } else {
                    isButtonEnable = true
                    showToast(
                        title = response.data?.message ?: "",
                        isSuccess = false
                    )
                }
            }

            is NetworkResult.Error<*> -> {
                isLoading = false
                isButtonEnable = true
                setNewPasswordViewModel.resetResponse()
                val message: String = try {
                    val jObjError =
                        JSONObject(response.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    response.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message,
                    isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                isLoading = true
                isButtonEnable = false
                setNewPasswordViewModel.resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                isButtonEnable = true
                isLoading = false
                setNewPasswordViewModel.resetResponse()
                showToast(
                    title = response.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha.value)
                .offset {
                    IntOffset(
                        offsetX.value.toInt(),
                        offsetY.value.toInt()
                    )
                }
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.sdp, top = 10.sdp)
            ) {
                DefaultBackArrow {
                    onBackPress()
                    navController?.popBackStack()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 15.sdp, end = 10.sdp, top = 15.sdp),
                contentAlignment = Alignment.TopStart,

                ) {

                Column {
                    HeadingTextComponentWithoutFullWidth(
                        value = stringResource(id = R.string.set_up_new_password),
                        textSize = 20.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.heightIn(),
                    )

                    Spacer(
                        modifier = Modifier.height(20.sdp)
                    )

                    NormalTextComponentWithoutFullWidth(
                        value = stringResource(id = R.string.create_your_password),
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        textSize = 12.ssp,
                        fontStyle = FontStyle.Normal,
                        modifier = Modifier.heightIn(min = 40.dp),
                    )

                    Column(modifier = Modifier.padding(top = 10.sdp)) {
                        PasswordTextFieldComponent(
                            labelValue = stringResource(id = R.string.new_password),
                            onTextSelected = {
                                setNewPasswordViewModel.onEvent(
                                    SetNewPasswordUIEvent.PasswordChanged(
                                        it
                                    )
                                )
                            },
                            errorStatus = setNewPasswordViewModel.setUpNewPasswordUIState.value.passwordError,
                            errorText = setNewPasswordViewModel.setUpNewPasswordUIState.value.passwordErrorMessage,
                            stringValue = setNewPasswordViewModel.setUpNewPasswordUIState.value.password,
                            unFocusColor = MaterialTheme.colorScheme.onTertiary,
                            focusColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 2.sdp)
                        )
                        Spacer(modifier = Modifier.height(7.sdp))

                        PasswordTextFieldComponent(
                            labelValue = stringResource(id = R.string.confirm_new_password),
                            onTextSelected = {
                                setNewPasswordViewModel.onEvent(
                                    SetNewPasswordUIEvent.ConfirmPasswordChanged(
                                        it
                                    )
                                )
                            },
                            errorStatus = setNewPasswordViewModel.setUpNewPasswordUIState.value.confirmPasswordError,
                            errorText = setNewPasswordViewModel.setUpNewPasswordUIState.value.confirmPasswordErrorMessage,
                            stringValue = setNewPasswordViewModel.setUpNewPasswordUIState.value.confirmPassword,
                            unFocusColor = MaterialTheme.colorScheme.onTertiary,
                            focusColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 2.sdp),
                            keyboardActions = ImeAction.Done
                        )

                    }

                }

                ButtonComponent(
                    value = stringResource(id = R.string.next),
                    onButtonClicked = {
                        setNewPasswordViewModel.onEvent(SetNewPasswordUIEvent.NextButtonClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .heightIn(45.sdp)
                        .padding(bottom = 20.sdp),
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.background,
                    isLoading = isLoading,
                    isEnabled = isButtonEnable

                )

                if (showBottomSheet.value) {
                    showBottomSheet = ForgotPasswordChangeSuccessBottomSheet(showBottomSheet) {
                        dismissListener.invoke()
                    }
                }


            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun SetUpNewPasswordScreenPreview() {
//    ConfirmYourEmailScreen()
}
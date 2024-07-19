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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.AuthHeaderComponent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.TextFieldComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.data.verifyEmail.VerifyEmailUIEvent
import com.cp.brittany.dixon.ui.viewModel.auth.VerifyEmailViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.modifiers.bounceClick
import com.cp.brittany.dixon.utills.sdp
import org.json.JSONObject


@Composable
inline fun VerifyYourEmailScreen(
    navController: NavController? = null,
    verifyEmailViewModel: VerifyEmailViewModel = hiltViewModel(),
    onClickVerify: () -> Unit,
) {
    val alpha = remember {
        Animatable(0f)
    }
    val context = LocalContext.current
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }
    var isLoading by remember { mutableStateOf(false) }

    val response by verifyEmailViewModel.verifyEmailResponse.collectAsStateWithLifecycle()
    val sendOtpResponse by verifyEmailViewModel.sendOtpResponse.collectAsStateWithLifecycle()
    var isButtonEnable by remember { mutableStateOf(true) }

    when (response) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (response.data?.status == true) {
                onClickVerify()
                showToast(
                    title = response.data?.message ?: "", isSuccess = true
                )
            } else {
                isButtonEnable = true
                showToast(
                    title = response.data?.message ?: "", isSuccess = false
                )
            }
            verifyEmailViewModel.resetResponse()

        }

        is NetworkResult.Error<*> -> {
            isLoading = false
            isButtonEnable = true
            verifyEmailViewModel.resetResponse()
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
            verifyEmailViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            isButtonEnable = true
            verifyEmailViewModel.resetResponse()

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }
    when (sendOtpResponse) {
        is NetworkResult.Success<*> -> {
            verifyEmailViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            verifyEmailViewModel.resetResponse()
            val message: String = try {
                val jObjError = JSONObject(sendOtpResponse.message.toString())
                jObjError.get("message").toString()
            } catch (e: Exception) {
                sendOtpResponse.message ?: context.resources.getString(
                    R.string.something_went_wrong
                )
            }

            showToast(
                title = message, isSuccess = false
            )
        }

        is NetworkResult.Loading<*> -> {
            verifyEmailViewModel.resetResponse()

        }

        is NetworkResult.NoInternet<*> -> {
            verifyEmailViewModel.resetResponse()
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

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.sdp)
            .alpha(alpha.value)
            .offset {
                IntOffset(
                    offsetX.value.toInt(), offsetY.value.toInt()
                )
            }) {
            AuthHeaderComponent(
                title = stringResource(id = R.string.verify_your_email),
                subtitle = stringResource(id = R.string.confirm_first_text),
                logo = R.drawable.logo_glampian,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.sdp)
            )
            Spacer(modifier = Modifier.height(10.sdp))

            TextFieldComponent(
                labelValue = stringResource(id = R.string.code),
                onTextChanged = {
                    verifyEmailViewModel.onEvent(VerifyEmailUIEvent.OtpCodeChanged(it))
                },
                errorStatus = verifyEmailViewModel.verifyEmailUIState.value.otpCodeError,
                errorText = verifyEmailViewModel.verifyEmailUIState.value.otpCodeErrorMessage,
                stringValue = verifyEmailViewModel.verifyEmailUIState.value.otpCode,
                unFocusColor = MaterialTheme.colorScheme.onTertiary,
                focusColor = MaterialTheme.colorScheme.secondary,
                keyboardType = KeyboardType.Number,
                keyboardActions = ImeAction.Done,
                errorModifier = Modifier.padding(start = 4.sdp, top = 5.sdp)
            )
        }

        ButtonComponent(value = stringResource(id = R.string.next),
            onButtonClicked = {
                verifyEmailViewModel.onEvent(VerifyEmailUIEvent.VerifyButtonClicked)
            },
            modifier = Modifier
                .bounceClick()
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .heightIn(45.sdp)
                .alpha(alpha.value)
                .offset {
                    IntOffset(
                        offsetX.value.toInt(), offsetY.value.toInt()
                    )
                }
                .padding(bottom = 20.sdp, start = 13.sdp, end = 13.sdp),
            buttonColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.background,
            isLoading = isLoading)

    }


}

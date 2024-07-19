package com.cp.brittany.dixon.ui.screens.auth.forgotPassword

import android.widget.Toast
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
import com.cp.brittany.dixon.ui.components.TextFieldComponent
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.data.forgotPassword.addEmail.ForgotPasswordUIEvent
import com.cp.brittany.dixon.ui.viewModel.auth.forgotPassword.ForgotPasswordViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.modifiers.bounceClick
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import org.json.JSONObject

@Composable
inline fun ForgotPasswordScreen(
    navController: NavController? = null,
    crossinline changeProgress: () -> Unit,
    crossinline onBackPress: () -> Unit,
    crossinline moveNextScreen: (String) -> Unit,
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    BackHandler(true) {
//        navController?.popBackStack()
        onBackPress()
    }

    val coroutineScope = rememberCoroutineScope()
    var isButtonEnable by remember { mutableStateOf(true) }

    val alpha = remember {
        Animatable(0f)
    }
    var isLoading by remember { mutableStateOf(false) }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }
    val response by forgotPasswordViewModel.forgotPasswordResponse.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
                    changeProgress()
                    moveNextScreen(forgotPasswordViewModel.forgotPasswordUIState.value.email)
                    forgotPasswordViewModel.resetResponse()
                    Toast.makeText(context, response.data?.message ?: "", Toast.LENGTH_SHORT).show()
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
                forgotPasswordViewModel.resetResponse()
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
                isButtonEnable = false
                forgotPasswordViewModel.resetResponse()
                isLoading = true
            }

            is NetworkResult.NoInternet<*> -> {
                isLoading = false
                isButtonEnable = true
                forgotPasswordViewModel.resetResponse()

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
                        value = stringResource(id = R.string.forgot_password), textSize = 20.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.heightIn(),
                    )

                    Spacer(
                        modifier = Modifier.height(20.sdp)
                    )

                    NormalTextComponentWithoutFullWidth(
                        value = stringResource(id = R.string.no_worries),
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        textSize = 12.ssp,
                        fontStyle = FontStyle.Normal,
                        modifier = Modifier.heightIn(min = 40.dp),
                    )

                    Box(modifier = Modifier.padding(top = 10.sdp)) {

                        TextFieldComponent(
                            labelValue = stringResource(id = R.string.email),
                            onTextChanged = {
                                forgotPasswordViewModel.onEvent(
                                    ForgotPasswordUIEvent.EmailChanged(
                                        it
                                    )
                                )
                            },
                            errorStatus = forgotPasswordViewModel.forgotPasswordUIState.value.emailError,
                            errorText = forgotPasswordViewModel.forgotPasswordUIState.value.emailErrorMessage,
                            stringValue = forgotPasswordViewModel.forgotPasswordUIState.value.email,
                            unFocusColor = MaterialTheme.colorScheme.onTertiary,
                            focusColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 2.sdp),
                            keyboardActions = ImeAction.Done,
                            errorModifier = Modifier.padding(start = 4.sdp, top = 5.sdp)

                        )

                    }

                }

                ButtonComponent(
                    value = stringResource(id = R.string.next),
                    onButtonClicked = {
                        forgotPasswordViewModel.onEvent(ForgotPasswordUIEvent.NextButtonClicked)
                    },
                    modifier = Modifier
                        .bounceClick()
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .heightIn(45.sdp)
                        .padding(bottom = 20.sdp),
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.background,
                    isLoading = isLoading,
                    isEnabled = isButtonEnable
                )


            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
//    ForgotPasswordScreen((0))
}
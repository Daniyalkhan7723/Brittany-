package com.cp.brittany.dixon.ui.screens.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.resetPassword.ResetPasswordUIEvent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.PasswordTextFieldComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.profile.ResetPasswordViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import org.json.JSONObject

@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(), onBackPress: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember {
        mutableStateOf(false)
    }
    val response by resetPasswordViewModel.resetPasswordResponse.collectAsStateWithLifecycle()
    var isButtonEnable by remember { mutableStateOf(true) }

    when (response) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (response.data?.status == true) {
                showToast(
                    title = response.data?.message ?: "", isSuccess = true
                )
                onBackPress()
            } else {
                isButtonEnable = true
                showToast(
                    title = response.data?.message ?: "", isSuccess = false
                )
            }
        }

        is NetworkResult.Error<*> -> {
            isLoading = false
            isButtonEnable = true
            resetPasswordViewModel.resetResponse()
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
            resetPasswordViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            isLoading = true
            resetPasswordViewModel.resetResponse()
            showToast(
                title = response.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    BrittanyDixonTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 20.sdp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart,
        ) {
            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(25.sdp))

                ProfileTopBar(
                    title = stringResource(R.string.change_password), backPress = onBackPress
                )
                Spacer(modifier = Modifier.height(30.sdp))
                Column(modifier = Modifier.padding(horizontal = 10.sdp)) {
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.old_password),
                        onTextSelected = {
                            resetPasswordViewModel.onEvent(
                                ResetPasswordUIEvent.OldPasswordChanged(
                                    it
                                )
                            )
                        },
                        errorStatus = resetPasswordViewModel.resetPasswordUIState.value.oldPasswordError,
                        errorText = resetPasswordViewModel.resetPasswordUIState.value.oldPasswordErrorMessage,
                        stringValue = resetPasswordViewModel.resetPasswordUIState.value.oldPassword,
                        unFocusColor = MaterialTheme.colorScheme.onTertiary,
                        focusColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 2.sdp),
                    )
                    Spacer(modifier = Modifier.height(15.sdp))
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.new_password),
                        onTextSelected = {
                            resetPasswordViewModel.onEvent(
                                ResetPasswordUIEvent.NewPasswordChanged(
                                    it
                                )
                            )
                        },
                        errorStatus = resetPasswordViewModel.resetPasswordUIState.value.newPasswordError,
                        errorText = resetPasswordViewModel.resetPasswordUIState.value.newPasswordErrorMessage,
                        stringValue = resetPasswordViewModel.resetPasswordUIState.value.newPassword,
                        unFocusColor = MaterialTheme.colorScheme.onTertiary,
                        focusColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 2.sdp),
                    )

                    Spacer(modifier = Modifier.height(15.sdp))

                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.confirm_new_password),
                        onTextSelected = {
                            resetPasswordViewModel.onEvent(
                                ResetPasswordUIEvent.ConfirmNewPasswordChanged(
                                    it
                                )
                            )
                        },
                        errorStatus = resetPasswordViewModel.resetPasswordUIState.value.confirmNewPasswordError,
                        errorText = resetPasswordViewModel.resetPasswordUIState.value.confirmNewPasswordErrorMessage,
                        stringValue = resetPasswordViewModel.resetPasswordUIState.value.confirmNewPassword,
                        unFocusColor = MaterialTheme.colorScheme.onTertiary,
                        focusColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 2.sdp),
                        keyboardActions = ImeAction.Done
                    )
                }
            }
            ButtonComponent(
                value = stringResource(R.string.next),
                onButtonClicked = {
                    resetPasswordViewModel.onEvent(ResetPasswordUIEvent.NextButtonClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .heightIn(45.sdp)
                    .padding(bottom = 20.sdp, start = 15.sdp, end = 15.sdp),
                buttonColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.background,
                isLoading = isLoading
            )
        }
    }

}
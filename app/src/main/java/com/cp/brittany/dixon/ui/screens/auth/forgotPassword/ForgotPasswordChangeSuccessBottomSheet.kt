package com.cp.brittany.dixon.ui.screens.auth.forgotPassword

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun ForgotPasswordChangeSuccessBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    crossinline dismissListener: () -> Unit
): MutableState<Boolean> {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch { modalBottomSheetState.hide() }
    }
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet.value = false
        },
        sheetState = modalBottomSheetState,
        dragHandle = null,
        content = {
            PasswordResetSuccessfullyScreen {
                coroutineScope.launch {
                    dismissListener()
                    modalBottomSheetState.hide()
                }
            }
        },
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.secondary,
    )
    return showBottomSheet

}
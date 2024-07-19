package com.cp.brittany.dixon.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.ssp

@Composable
fun LogoutDialogueComponent(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    titleText: String,
    headingText: String
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            BoldTextComponent(
                value = titleText,
                textSize = 14.ssp,
                textColor = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            HeadingTextComponentWithoutFullWidth(
                value = headingText,
                textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.secondaryContainer
            )

        },
        confirmButton = {
            TextButton(onClick = { onConfirmClick() }) {
                HeadingTextComponentWithoutFullWidth(
                    value = stringResource(R.string.yes),
                    textSize = 11.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                HeadingTextComponentWithoutFullWidth(
                    value = stringResource(R.string.no),
                    textSize = 11.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}
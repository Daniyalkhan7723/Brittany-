package com.cp.brittany.dixon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.openAppSettings

@Composable
fun PermissionDialogueComponent(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val click by remember {
        mutableStateOf(Modifier.clickable {
            onDismissRequest()
        })
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(20.sdp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                modifier = Modifier
                    .size(30.sdp)
                    .padding(end = 10.sdp, top = 5.sdp)
                    .then(click)
                    .align(Alignment.TopEnd),
                contentDescription = "close"
            )
            Column(
                modifier = Modifier.padding(
                    start = 16.sdp,
                    end = 16.sdp,
                    bottom = 16.sdp,
                    top = 25.sdp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(5.sdp))

                BoldTextComponent(
                    value = "Permission Required",
                    textSize = 14.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.sdp))

                HeadingTextComponentWithoutFullWidth(
                    value = "To use glampion's you need to enable your camera and storage permissions to access gallery",
                    textSize = 12.ssp,
                    textColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.width(200.sdp)
                )

                Spacer(modifier = Modifier.height(20.sdp))


                Button(
                    modifier = Modifier.width(230.sdp),
                    onClick = {
                        context.findActivity()?.openAppSettings()
                        onDismissRequest()
                    },
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    shape = RoundedCornerShape(48.sdp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(45.sdp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(48.sdp)
                            ), contentAlignment = Alignment.Center
                    ) {

                        HeadingTextComponentWithoutFullWidth(
                            value = "Open Settings",
                            textSize = 13.ssp,
                            textColor = MaterialTheme.colorScheme.background,
                        )

                    }

                }


            }
        }

    }
}

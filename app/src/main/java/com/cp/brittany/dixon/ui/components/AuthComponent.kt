package com.cp.brittany.dixon.ui.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.AuthResultContract
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun AuthHeaderComponent(
    title: String,
    subtitle: String,
    logo: Int,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = logo),
            contentDescription = null,
            modifier = Modifier.size(80.sdp),
            contentScale = ContentScale.FillBounds,
        )
    }

    Spacer(modifier = Modifier.height(15.sdp))

    Column(modifier = textModifier) {
        HeadingTextComponent(
            value = title,
            textSize = 20.ssp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.sdp)
                .heightIn(),
        )
        Spacer(modifier = Modifier.height(20.sdp))

        NormalTextComponent(
            value = subtitle,
            modifier = Modifier
                .padding(horizontal = 10.sdp)
                .heightIn(min = 40.dp),
            textSize = 12.ssp,
            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }

}


@Composable
fun SocialMediaSection(
    googleSignInClient: GoogleSignInClient,
    loginManager: LoginManager,
    onGoogleSignInCompleted: (GoogleSignInAccount) -> Unit,
    onGoogleSignInError: () -> Unit,
    loadingForSocial: (Boolean) -> Unit,
) {

    //Google Login
    val coroutineScope = rememberCoroutineScope()
    val signInRequestCode = 1

    //FaceBook Login
    val scope = rememberCoroutineScope()

    val callbackManager = remember { CallbackManager.Factory.create() }
    val fbLauncher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {

    }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        //Google Login
        val authResultLauncher =
            rememberLauncherForActivityResult(contract = AuthResultContract(googleSignInClient)) {
                loadingForSocial(false)
                try {
                    val account = it?.getResult(ApiException::class.java)
                    if (account == null) {
                        onGoogleSignInError()
                    } else {
                        coroutineScope.launch {
                            onGoogleSignInCompleted(account)
                        }
                    }
                } catch (e: ApiException) {
                    onGoogleSignInError()
                }
            }

        //FaceBook Login
        DisposableEffect(Unit) {
            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("onCancellllll","Cancel")
                    // do nothing
                }

                override fun onError(error: FacebookException) {
                    Log.d("onErorrrr",error.message.toString())
                }

                override fun onSuccess(result: LoginResult) {
                    scope.launch {
                        val token = result.accessToken.token

                    }
                }
            })

            onDispose {
                loginManager.unregisterCallback(callbackManager)
            }
        }


        SocialButton(icon = R.drawable.ic_google,
            text = stringResource(id = R.string.google),
            modifier = Modifier
                .width(105.sdp)
                .height(35.sdp),
            imageSize = 14.sdp,
            onClick = {
                loadingForSocial(true)
                authResultLauncher.launch(signInRequestCode)
            })

        Spacer(modifier = Modifier.width(10.sdp))

        SocialButton(icon = R.drawable.ic_fb,
            text = stringResource(id = R.string.facebook),
            modifier = Modifier
//                .weight(1f)
                .width(105.sdp)
                .height(35.sdp),
            imageSize = 17.sdp,
            onClick = {
                fbLauncher.launch(listOf("email", "public_profile"))
            })

    }
}


@Composable
fun LoginFacebookButton(
    onAuthComplete: () -> Unit,
    onAuthError: (Exception) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {

    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {
                onAuthError(error)
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    onAuthComplete()

                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }
}
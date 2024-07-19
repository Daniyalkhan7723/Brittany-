package com.cp.brittany.dixon.ui.screens.home.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileItem
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.profile.ProfileViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@Composable
inline fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    crossinline navigateToProfileSettingScreen: () -> Unit
) {
    val context = LocalContext.current
    val goToProfileScreen = remember {
        {

        }
    }

    val getProfileResponse by profileViewModel.getProfile.collectAsStateWithLifecycle()

    when (getProfileResponse) {
        is NetworkResult.Success<*> -> {
            if (getProfileResponse.data?.status == true) {
                getProfileResponse.data?.data?.let {
                    profileViewModel.getProfileResponseData.value = it
                }
            } else {
                showToast(
                    title = getProfileResponse.data?.message ?: "", isSuccess = false
                )
            }
            profileViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            profileViewModel.resetResponse()
            val message: String = try {
                val jObjError = JSONObject(getProfileResponse.message.toString())
                jObjError.get("message").toString()
            } catch (e: Exception) {
                getProfileResponse.message ?: context.resources.getString(
                    R.string.something_went_wrong
                )
            }

            showToast(
                title = message, isSuccess = false
            )
        }

        is NetworkResult.Loading<*> -> {
            profileViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            profileViewModel.resetResponse()
            showToast(
                title = getProfileResponse.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    BrittanyDixonTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = MaterialTheme.colorScheme.background
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileItem(
                icon = R.drawable.ic_settings,
                goProfileScreen = goToProfileScreen,
                screenType = Constants.PROFILE_SCREEN,
                clickModifier = Modifier.clickable {
                    navigateToProfileSettingScreen()
                },
                data = profileViewModel.preference.getUser()
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 30.sdp)) {
                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 20.sdp,
                    progress = { 0.45f },
                    modifier = Modifier.then(Modifier.size(100.sdp)),
                    color = MaterialTheme.colorScheme.onSecondary,
                    trackColor = MaterialTheme.colorScheme.onPrimary

                )
                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round,
                    progress = { 0.55f },
                    strokeWidth = 20.sdp,
                    modifier = Modifier.then(Modifier.size(160.sdp)),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    trackColor = MaterialTheme.colorScheme.onPrimary
                )

                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round,
                    progress = { 0.75f },
                    strokeWidth = 20.sdp,
                    modifier = Modifier.then(Modifier.size(220.sdp)),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.onPrimary

                )
            }
        }
    }
}

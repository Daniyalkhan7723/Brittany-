package com.cp.brittany.dixon.ui.screens.home.profile

import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.profileSettings.ProfileSettingEvent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileItem
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.viewModel.profile.ProfileSettingViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.DetailRoute
import com.cp.brittany.dixon.ui.components.LogoutDialogueComponent
import com.cp.brittany.dixon.ui.components.findActivity
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileSettingComponent
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.ChooseSubscription
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileSettingScreen(
    navController: NavHostController,
    profileSettingViewModel: ProfileSettingViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onBackPress: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val appCompactActivity = LocalContext.current.findActivity()

    val callback = remember {
        {
            navController.navigate(DetailRoute.ProfileEdit.route)
        }
    }
    val logoutResponse by profileSettingViewModel.logoutResponse.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val backPress = remember {
        {
            onBackPress()

        }
    }

    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ChooseSubscription.billingClient != null) {
                    ChooseSubscription.billingClient?.endConnection()
                }
                onBackPress()

            }
        }
    }
    val onBackPressedDispatcher = appCompactActivity?.onBackPressedDispatcher
    DisposableEffect(onBackPressedDispatcher) {
        onBackPressedDispatcher?.addCallback(onBackPressedCallback)
        onDispose { onBackPressedCallback.remove() }
    }


    LaunchedEffect(key1 = Unit) {
        profileSettingViewModel.billingSetup()
    }

    when (logoutResponse) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (logoutResponse.data?.status == true) {
                context.imageLoader.diskCache?.clear()
                context.imageLoader.memoryCache?.clear()
                profileSettingViewModel.preference.clearAllPreferenceData()
                onLogout()
            } else {
                showToast(
                    title = logoutResponse.data?.message ?: "", isSuccess = false
                )
            }
            profileSettingViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            isLoading = false
            profileSettingViewModel.resetResponse()
            val message: String = try {
                val jObjError = JSONObject(logoutResponse.message.toString())
                jObjError.get("message").toString()
            } catch (e: Exception) {
                logoutResponse.message ?: context.resources.getString(
                    R.string.something_went_wrong
                )
            }

            showToast(
                title = message, isSuccess = false
            )
        }

        is NetworkResult.Loading<*> -> {
            isLoading = true
            profileSettingViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            profileSettingViewModel.resetResponse()
            showToast(
                title = logoutResponse.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    BrittanyDixonTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
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
                    title = stringResource(R.string.profile_settings),
                    backPress = backPress,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(20.sdp))
                ProfileItem(
                    icon = R.drawable.ic_more,
                    goProfileScreen = callback,
                    modifier = Modifier
                        .padding(horizontal = 15.sdp)
                        .clip(RoundedCornerShape(13.sdp)),
                    screenType = Constants.PROFILE_SETTING_SCREEN,
                    data = profileSettingViewModel.preference.getUser(),

                    )

                Spacer(modifier = Modifier.height(15.sdp))
                ProfileSettingComponent(
                    modifier = Modifier
                        .padding(horizontal = 15.sdp)
                        .clip(RoundedCornerShape(13.sdp)), navController = navController,
                    purchaseDetailsList = profileSettingViewModel.subscriptionProductList
                )

            }

            ButtonComponent(
                value = stringResource(id = R.string.logout),
                onButtonClicked = {
                    isLoading = true
                    showLogoutDialog = !showLogoutDialog
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
            if (showLogoutDialog) {
                LogoutDialogueComponent(onConfirmClick = {
                    profileSettingViewModel.onEvent(ProfileSettingEvent.LogoutButton)
                    showLogoutDialog = false
                }, onDismissRequest = {
                    showLogoutDialog = false
                    isLoading = false
                }, titleText = stringResource(id = R.string.logout),
                    headingText = stringResource(R.string.are_you_sure_you_want_to_logout)
                )

            }
        }

    }


}
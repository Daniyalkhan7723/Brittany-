package com.cp.brittany.dixon.ui.screens.home.profile

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.TextFieldComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.data.editProfile.EditProfileEvent
import com.cp.brittany.dixon.ui.components.ChooseImageDialogueComponent
import com.cp.brittany.dixon.ui.components.PermissionDialogueComponent
import com.cp.brittany.dixon.ui.viewModel.profile.EditProfileViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.screens.home.profile.profileComposables.ProfileTopBar
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Utils.getFilePath
import com.cp.brittany.dixon.utills.Utils.newPermissionsRequired
import com.cp.brittany.dixon.utills.Utils.oldPermissionsRequired
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@Composable
fun ProfileEditScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(), onBackPress: () -> Unit
) {
    val askPermissions = arrayListOf<String>()
    val response by editProfileViewModel.editProfileResponse.collectAsStateWithLifecycle()
    val avatarResponse by editProfileViewModel.addImageResponse.collectAsStateWithLifecycle()
    var showImageChooserDialog by remember { mutableStateOf(false) }
    var permissionDialog by remember { mutableStateOf(false) }
    var isButtonEnable by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var imageRequest by remember { mutableStateOf(Any()) }


    var allPermissionGranted by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val getUriListResult = remember<(List<Uri>) -> Unit> {
        {

        }
    }


    val permissionsLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
            allPermissionGranted = !permissionsMap.containsValue(false)
            if (allPermissionGranted) {
                showImageChooserDialog = !showImageChooserDialog
            } else {
                permissionDialog = !permissionDialog
            }
        }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        for (permission in newPermissionsRequired) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askPermissions.add(permission)
            }
        }
    } else {
        for (permission in oldPermissionsRequired) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askPermissions.add(permission)
            }
        }
    }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val backPress = remember {
        {
            onBackPress()
        }
    }

    val onClickOpenImageDialogue by remember {
        mutableStateOf(Modifier.clickable {
            if (!allPermissionGranted) {
                permissionsLauncher.launch(askPermissions.toTypedArray())
            } else {
                showImageChooserDialog = !showImageChooserDialog
            }
        })
    }


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
            editProfileViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            isLoading = false
            editProfileViewModel.resetResponse()
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
            isLoading = true
            isButtonEnable = false
            editProfileViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            isButtonEnable = true
            editProfileViewModel.resetResponse()
            showToast(
                title = response.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    when (avatarResponse) {
        is NetworkResult.Success<*> -> {
            isLoading = false
            if (avatarResponse.data?.status == true) {
                showToast(
                    title = avatarResponse.data?.message ?: "", isSuccess = true
                )
                onBackPress()
            } else {
                isButtonEnable = true
                showToast(
                    title = avatarResponse.data?.message ?: "", isSuccess = false
                )
            }
            editProfileViewModel.resetResponse()
        }

        is NetworkResult.Error<*> -> {
            isButtonEnable = true
            isLoading = false
            editProfileViewModel.resetResponse()
            val message: String = try {
                val jObjError = JSONObject(avatarResponse.message.toString())
                jObjError.get("message").toString()
            } catch (e: Exception) {
                avatarResponse.message ?: context.resources.getString(
                    R.string.something_went_wrong
                )
            }
            showToast(
                title = message, isSuccess = false
            )
        }

        is NetworkResult.Loading<*> -> {
            isLoading = true
            isButtonEnable = false
            editProfileViewModel.resetResponse()
        }

        is NetworkResult.NoInternet<*> -> {
            isLoading = false
            isButtonEnable = true
            editProfileViewModel.resetResponse()
            showToast(
                title = avatarResponse.message ?: "", isSuccess = false
            )

        }

        is NetworkResult.NoCallYet<*> -> {

        }
    }

    LaunchedEffect(key1 = Unit) {
        val listener = object : ImageRequest.Listener {
            override fun onError(request: ImageRequest, result: ErrorResult) {
                super.onError(request, result)
            }

            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                super.onSuccess(request, result)
            }
        }
        imageRequest =
            ImageRequest.Builder(context).data(editProfileViewModel.preference.getUser()?.avatar)
                .listener(listener)
                .memoryCacheKey(editProfileViewModel.preference.getUser()?.avatar)
                .diskCacheKey(editProfileViewModel.preference.getUser()?.avatar)
                .placeholder(R.drawable.place_holder).error(R.drawable.place_holder)
                .fallback(R.drawable.place_holder).diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED).crossfade(true).build()
    }

    BrittanyDixonTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.sdp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart,
        ) {
            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(25.sdp))
                ProfileTopBar(
                    title = stringResource(R.string.edit_profile), backPress = backPress
                )
                Spacer(modifier = Modifier.height(30.sdp))

                Column {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Image",
                            modifier = Modifier
                                .size(80.sdp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.sdp)
                    ) {
                        Box(
                            modifier = Modifier
                                .then(onClickOpenImageDialogue)
                        ) {
                            Text(
                                text = stringResource(R.string.upload_new_photo),
                                style = TextStyle(
                                    fontSize = 14.ssp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                ),
                            )
                        }
                    }


                    if (showImageChooserDialog) {
                        ChooseImageDialogueComponent(onDismissRequest = {
                            showImageChooserDialog = false
                        }, resultUri = { uri ->
                            capturedImageUri = uri
                            editProfileViewModel.onEvent(
                                EditProfileEvent.GetImageButtonClicked(
                                    getFilePath(
                                        context, uri
                                    )
                                )
                            )
                        }, resultListUri = getUriListResult, screenType = Constants.PROFILE_SCREEN)
                    }

                    if (permissionDialog) {
                        PermissionDialogueComponent(onDismissRequest = {
                            permissionDialog = false
                        })
                    }

                    TextFieldComponent(
                        labelValue = stringResource(id = R.string.name),
                        onTextChanged = {
                            editProfileViewModel.onEvent(EditProfileEvent.NameChanged(it))
                        },
                        errorStatus = editProfileViewModel.editProfileUIState.value.fullNameError,
                        errorText = editProfileViewModel.editProfileUIState.value.fullNameErrorMessage,
                        stringValue = editProfileViewModel.editProfileUIState.value.fullName,
                        unFocusColor = MaterialTheme.colorScheme.onTertiary,
                        focusColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 13.sdp),
                        keyboardActions = ImeAction.Done,
                        errorModifier = Modifier.padding(start = 13.sdp, top = 7.sdp)
                    )
                }
            }

            ButtonComponent(
                value = stringResource(R.string.save_changes),
                onButtonClicked = {
                    editProfileViewModel.onEvent(EditProfileEvent.EditProfileButtonClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .heightIn(45.sdp)
                    .padding(bottom = 20.sdp, start = 15.sdp, end = 15.sdp),
                buttonColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.background,
                isLoading = isLoading,
                isEnabled = isButtonEnable
            )
        }


    }
}

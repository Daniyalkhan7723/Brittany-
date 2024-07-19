package com.cp.brittany.dixon.ui.screens.home.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.DeleteImageUIEvent
import com.cp.brittany.dixon.model.shared.ImagesSharedModel
import com.cp.brittany.dixon.ui.components.LogoutDialogueComponent
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.calendar.PreviewImagesViewModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.photo.ExperimentalPhotoApi
import com.cp.brittany.dixon.utills.photo.PhotoBox
import com.cp.brittany.dixon.utills.photo.rememberPhotoState
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@OptIn(ExperimentalPhotoApi::class)
@Composable
fun PreviewImageScreen(
    onBackPress: () -> Unit,
    imageUrl: String,
    imageId: String,
    previewImageViewModel: PreviewImagesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
) {
    val deleteImageResponse by previewImageViewModel.deleteImagesResponse.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var workoutByDateLoaderState by remember {
        mutableStateOf(false)
    }


    if (previewImageViewModel.deleteImagesApiHit.value) {
        when (deleteImageResponse) {
            is NetworkResult.Success<*> -> {
                previewImageViewModel.deleteImagesApiHit.value = false
                workoutByDateLoaderState = false
                if (deleteImageResponse.data?.status == true) {
                    showToast(
                        title = deleteImageResponse.data?.message ?: "",
                        isSuccess = true
                    )
                    sharedViewModel.refreshWorkoutImagesWhenSingleImageIsDelete(
                        ImagesSharedModel(
                            deleteOrNot = true,
                            imageId = imageId.toInt()
                        )
                    )
                    onBackPress()
                } else {
                    showToast(
                        title = deleteImageResponse.data?.message ?: "",
                        isSuccess = false
                    )
                }
                previewImageViewModel.resetResponse()
            }

            is NetworkResult.Error<*> -> {
                workoutByDateLoaderState = false
                previewImageViewModel.deleteImagesApiHit.value = false
                previewImageViewModel.resetResponse()
                val message: String = try {
                    val jObjError =
                        JSONObject(deleteImageResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    deleteImageResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                workoutByDateLoaderState = true
                previewImageViewModel.resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                workoutByDateLoaderState = false
                previewImageViewModel.deleteImagesApiHit.value = false
                previewImageViewModel.resetResponse()
                showToast(
                    title = deleteImageResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }
    }

    BrittanyDixonTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        val imageRequest = ImageRequest.Builder(context)
            .data(imageUrl)
            .placeholder(R.drawable.workout_place_holder).error(R.drawable.workout_place_holder)
            .fallback(R.drawable.workout_place_holder).crossfade(true).build()


        val onClickBackPress by remember {
            mutableStateOf(Modifier.clickable {
                onBackPress()
            })
        }

        val onClickDelete by remember {
            mutableStateOf(Modifier.clickable {
                showLogoutDialog = !showLogoutDialog
            })
        }

        val onClickYes = remember {
            {
                showLogoutDialog = false
                previewImageViewModel.onEvent(
                    DeleteImageUIEvent.DeleteImage(
                        imageId
                    )
                )
            }
        }

        val onClickDismiss = remember {
            {
                showLogoutDialog = false
            }
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top=20.sdp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart
        ) {
            val photoState = rememberPhotoState()
            PhotoBox(state = photoState) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "image",
                    modifier = Modifier.fillMaxSize(),
                    transform = {
                        if (it is AsyncImagePainter.State.Success) {
                            photoState.setPhotoIntrinsicSize(it.painter.intrinsicSize)
                        }
                        it
                    }
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.sdp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 2.sdp, start = 10.sdp)
                        .then(onClickBackPress)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .rotate(180f)
                            .size(17.sdp)
                    )
                    NormalTextComponentWithoutFullWidth(
                        value = stringResource(R.string.back), textSize = 12.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        fontStyle = FontStyle.Normal,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_thrash),
                    contentDescription = "delete",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 15.sdp)
                        .then(onClickDelete)
                        .size(22.sdp),
                )

            }


            if (showLogoutDialog) {
                LogoutDialogueComponent(
                    onConfirmClick = onClickYes,
                    onDismissRequest = onClickDismiss,
                    titleText = stringResource(id = R.string.delete_image),
                    headingText = stringResource(R.string.are_you_sure_you_want_to_delete)
                )

            }

            if (workoutByDateLoaderState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Alignment.Center),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }

}
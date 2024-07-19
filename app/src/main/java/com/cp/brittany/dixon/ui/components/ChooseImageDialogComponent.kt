package com.cp.brittany.dixon.ui.components

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import kotlinx.coroutines.launch
import java.io.File
import java.util.Objects
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import com.cp.brittany.dixon.utills.Constants

@Composable
fun ChooseImageDialogueComponent(
    onDismissRequest: () -> Unit,
    resultUri: (Uri) -> Unit,
    resultListUri: (List<Uri>) -> Unit,
    screenType: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var cameraUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var onResultUri = remember<(List<Uri>) -> Unit> {
        { uri ->
            scope.launch {
                resultListUri(uri)
                onDismissRequest()
            }
        }
    }

    val click by remember {
        mutableStateOf(Modifier.clickable {
            onDismissRequest()
        })
    }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }

    val multiplePhotosPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = 5
            ), onResult = onResultUri
        )


    LaunchedEffect(key1 = Unit) {
        val file = File(Utils.createImageFolder(context, ""), Utils.fileName)
        cameraUri = if (Utils.isNougat()) {
            FileProvider.getUriForFile(
                Objects.requireNonNull(context), context.packageName + ".provider", file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onDismissRequest()
                resultUri(cameraUri)
            }

        }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        try {
            val selectedImage: Uri
            uri?.let {
                selectedImage = it
                if (selectedImage != null && "content" == selectedImage.scheme) {
                    val cursor: Cursor? = context.contentResolver.query(
                        selectedImage,
                        arrayOf(MediaStore.Images.ImageColumns.DATA),
                        null,
                        null,
                        null
                    )
                    cursor?.moveToFirst()
                    cursor?.getString(
                        0
                    ) ?: ""
                    cursor?.close()
                } else {
                    selectedImage.path ?: ""
                }
                onDismissRequest()
                resultUri(uri)
            }

        } catch (ex: Exception) {
            Log.e("getUser error", ex.message.toString())
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        val paddingValues = PaddingValues(
            horizontal = 20.sdp, vertical = 12.sdp
        )
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(20.sdp)
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
                    start = 16.sdp, end = 16.sdp, bottom = 16.sdp, top = 25.sdp
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(5.sdp))

                BoldTextComponent(
                    value = stringResource(R.string.select_image),
                    textSize = 14.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.sdp))

                Button(
                    modifier = Modifier.width(230.sdp),
                    onClick = {
                        if (screenType == Constants.PROFILE_SCREEN) {
                            galleryLauncher.launch("image/*")
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (isPhotoPickerAvailable(context)) {
                                    multiplePhotosPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else {
                                    galleryLauncher.launch("image/*")
                                }
                            } else {
                                galleryLauncher.launch("image/*")
                            }

                        }

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
                            value = stringResource(R.string.gallery),
                            textSize = 13.ssp,
                            textColor = MaterialTheme.colorScheme.background,
                        )

                    }

                }

                Spacer(modifier = Modifier.height(15.sdp))

                BorderButtonRound(
                    value = stringResource(R.string.camera),
                    onButtonClicked = {
                        scope.launch {
                            cameraLauncher.launch(cameraUri)
                        }
                    },
                    paddingValues = paddingValues,
                    modifier = Modifier.width(230.sdp),
                    shapeRadius = 48,
                    borderRadius = 50
                )

            }
        }

    }
}

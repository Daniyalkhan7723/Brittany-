package com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.calendar.Images
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.ChooseImageDialogueComponent
import com.cp.brittany.dixon.ui.components.ClickableTextComposable
import com.cp.brittany.dixon.ui.components.PermissionDialogueComponent
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.dashedBorder
import com.cp.brittany.dixon.utills.getPathFromUris
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun TodayActivityItem(
    navigateToScheduleWorkoutsDetailsScreen: () -> Unit,
    workoutByDate: WorkoutData,
    listUri: (List<String>, Int, Int) -> Unit,
    index: Int
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    val askPermissions = arrayListOf<String>()

    val imageRequest = ImageRequest.Builder(context).data(workoutByDate.video?.thumbnail)
        .placeholder(R.drawable.place_holder_3).error(R.drawable.place_holder_3)
        .fallback(R.drawable.place_holder_3).crossfade(true).build()
    var permissionDialog by remember { mutableStateOf(false) }
    var showImageChooserDialog by remember { mutableStateOf(false) }
    var allPermissionGranted by remember {
        mutableStateOf(false)
    }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val getUriListResult = remember<(List<Uri>) -> Unit> {
        { uriList ->
            if (uriList.isNotEmpty()) {
                val images = activity.getPathFromUris(uriList)
                listUri(images, workoutByDate.id ?: 0, index)
                Log.d("PhotoPicker", "Number of items selected: ${uriList.size}")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    val resultUri = remember<(Uri) -> Unit> {
        { uri ->
            capturedImageUri = uri
            listUri(
                arrayListOf(
                    Utils.getFilePath(
                        context, uri
                    ) ?: ""
                ), workoutByDate.id ?: 0, index
            )
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
        for (permission in Utils.newPermissionsRequired) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askPermissions.add(permission)
            }
        }
    } else {
        for (permission in Utils.oldPermissionsRequired) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askPermissions.add(permission)
            }
        }
    }


    val onClickQuickProgress by remember {
        mutableStateOf(Modifier.clickable {
            if (!allPermissionGranted) {
                permissionsLauncher.launch(askPermissions.toTypedArray())
            } else {
                showImageChooserDialog = !showImageChooserDialog
            }
        })
    }

    val onClickItem by remember {
        mutableStateOf(Modifier.clickable {
            navigateToScheduleWorkoutsDetailsScreen()
        })
    }

    val onClickReadMore = remember<(String) -> Unit> {
        {
            navigateToScheduleWorkoutsDetailsScreen()
        }

    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 5.sdp, end = 5.sdp)
            .width(295.sdp)
            .then(onClickItem)
            .border(
                width = 1.sdp, color = Color(0xFF31363D), shape = RoundedCornerShape(percent = 5)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(9.sdp)
            ), contentAlignment = Center
    ) {

        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                Modifier
                    .padding(top = 10.sdp, bottom = 10.sdp, start = 10.sdp)
                    .fillMaxWidth()
            ) {
                // Load and display the image with AsyncImage
                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    modifier = Modifier
                        .width(71.sdp)
                        .height(58.sdp)
                        .clip(RoundedCornerShape(10.sdp)),
                    contentScale = ContentScale.Crop,
                )
                Box(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(start = 10.sdp, top = 4.sdp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = workoutByDate.title ?: "",
                            modifier = Modifier.padding(start = 4.sdp),
                            maxLines = 1,
                            style = TextStyle(
                                fontSize = 13.ssp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = AppFont.MyCustomFont
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.height(2.sdp))

                        ClickableTextComposable(modifier = Modifier.padding(start = 4.sdp),
                            description = workoutByDate.description ?: "",
                            onTextSelected = {
                                onClickReadMore(it)
                            })
                    }

                }

            }

            if (workoutByDate.images?.isEmpty() != true) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.sdp, end = 10.sdp)
                        .height((1.5).dp)
                        .background(Color(0xFF35393e))
                )

                setImages(workoutByDate.images)
            }


            Box(
                Modifier
                    .height(36.sdp)
                    .padding(horizontal = 10.sdp)
                    .fillMaxWidth()
                    .then(onClickQuickProgress)
                    .align(CenterHorizontally)
                    .dashedBorder(
                        strokeWidth = 1.sdp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        cornerRadiusDp = 25.sdp
                    ), contentAlignment = Center
            ) {
                Text(
                    text = stringResource(R.string.quick_progress_photos),
                    modifier = Modifier.align(Center),
                    style = TextStyle(
                        fontSize = 13.ssp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = Color(0xFFa09ca4),
                    ),
                    textAlign = TextAlign.Center
                )
            }

            if (showImageChooserDialog) {
                ChooseImageDialogueComponent(
                    onDismissRequest = {
                        showImageChooserDialog = false
                    },
                    resultUri = resultUri,
                    resultListUri = getUriListResult,
                    screenType = Constants.CALENDAR
                )
            }

            if (permissionDialog) {
                PermissionDialogueComponent(onDismissRequest = {
                    permissionDialog = false
                })
            }

            Spacer(Modifier.height(10.sdp))
        }
    }
}

@Composable
private fun setImages(
    images: MutableList<Images>?
) {
    val context = LocalContext.current
    var remainedImages by remember { mutableStateOf(0) }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.sdp)
            .padding(top = 10.sdp, bottom = 10.sdp, start = 10.sdp, end = 10.sdp)
    ) {
        items(count = images?.size ?: 0, key = {
            images?.get(it)?.id ?: 0
        }, itemContent = { index ->
            if (index <= 5) {
                val galleriesPhotos =
                    ImageRequest.Builder(context).data(images?.get(index)?.image ?: "")
                        .placeholder(R.drawable.place_holder_3).error(R.drawable.place_holder_3)
                        .fallback(R.drawable.place_holder_3).crossfade(true).build()

                Box {
                    if (index <= 4) {
                        AsyncImage(
                            model = galleriesPhotos,
                            contentDescription = null,
                            modifier = Modifier
                                .width(58.sdp)
                                .height(59.sdp)
                                .clip(RoundedCornerShape(7.sdp)),
                            contentScale = ContentScale.Crop,
                        )

                    }


                    if ((images?.size ?: 0) > 5) {
                        if (index == 4) Box(modifier = with(
                            Modifier
                                .width(60.sdp)
                                .height(59.sdp)
                        ) {
                            fillMaxSize().paint(
                                // Replace with your image id
                                painterResource(id = R.drawable.transparent_layout),
                                contentScale = ContentScale.FillBounds
                            )

                        }) {
                            remainedImages = images?.size?.minus(5) ?: 0
                            BoldTextComponent(
                                value = "+ $remainedImages",
                                textSize = 14.ssp,
                                textColor = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.align(Center)
                            )
                        }
                    }

                }
            } else {
                return@items
            }
        })

    }
}

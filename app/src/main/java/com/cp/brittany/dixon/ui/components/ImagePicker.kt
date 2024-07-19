package com.cp.brittany.dixon.ui.components

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.android.installreferrer.BuildConfig
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.utills.sdp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePicker() {
    val context = LocalContext.current
    var currentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    //var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) currentPhotoUri = uri
        }
    )

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                cameraLauncher.launch(uri)
            } else print("camera permission is denied")
        }
    )

}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}

private fun isNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun providerPath(context: Context): String {
    return context.packageName + ".fileprovider"
}

private fun createImageFolder(context: Context, path: String): String? {
    val folderPath = context.getExternalFilesDir("")
        ?.absolutePath + "/" + context.getString(R.string.app_name)
    try {
        val file = File("$folderPath/$path")
        if (!file.exists()) file.mkdirs()
        return file.absolutePath
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return folderPath
}
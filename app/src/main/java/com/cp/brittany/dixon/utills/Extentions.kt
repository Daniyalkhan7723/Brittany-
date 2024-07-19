package com.cp.brittany.dixon.utills

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Patterns
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.findActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Activity.shouldShowRationale(name: String): Boolean {
    return shouldShowRequestPermissionRationale(name)
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun String.getFormattedDate(): String {
    var localTime: LocalTime? = null
    var duration: Duration? = null
    var formattedDuration: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        localTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))
        duration = Duration.between(LocalTime.MIN, localTime)
        val hours: Long = duration?.toHours() ?: 0
        val minutes: Long = duration.minusHours(hours).toMinutes()
        formattedDuration = if (hours > 0) {
            String.format(Locale.getDefault(), "%d HOURS %d MIN", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%d MIN", minutes)
        }
    } else {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        try {
            val date = inputFormat.parse(this)

            // Extract hours and minutes
            val hours = date?.hours?.toLong()
            val minutes = date?.minutes?.toLong()

            // Build the formatted string based on hours and minutes
            if (hours != null) {
                formattedDuration = if (hours > 0) {
                    String.format(Locale.getDefault(), "%d hour %d minute", hours, minutes)
                } else {
                    String.format(Locale.getDefault(), "%d minute", minutes)
                }
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return formattedDuration ?: ""
}

fun String.getFormattedDateWithSec(): String {
    var localTime: LocalTime? = null
    var duration: Duration? = null
    var formattedDuration: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        localTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))
        duration = Duration.between(LocalTime.MIN, localTime)
        val hours: Long = duration?.toHours() ?: 0
        val minutes: Long = duration.minusHours(hours).toMinutes()
        val seconds = duration.seconds % 60

        formattedDuration = if (hours > 0) {
            String.format(Locale.getDefault(), "%d HOURS %d MIN", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%d min %d sec", minutes, seconds)
        }
    } else {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        try {
            val date = inputFormat.parse(this)

            // Extract hours and minutes
            val hours = date?.hours?.toLong()
            val minutes = date?.minutes?.toLong()

            // Build the formatted string based on hours and minutes
            if (hours != null) {
                formattedDuration = if (hours > 0) {
                    String.format(Locale.getDefault(), "%d hour %d minute", hours, minutes)
                } else {
                    String.format(Locale.getDefault(), "%d minute", minutes)
                }
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return formattedDuration ?: ""
}


fun String.getFormattedDateWithSmallMin(): String {
    var localTime: LocalTime?
    var duration: Duration?
    var formattedDuration: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        localTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))
        duration = Duration.between(LocalTime.MIN, localTime)
        val hours: Long = duration?.toHours() ?: 0
        val minutes: Long = duration.minusHours(hours).toMinutes()
        formattedDuration = if (hours > 0) {
            String.format(Locale.getDefault(), "%d hours %d min", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%d min", minutes)
        }
    } else {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        try {
            val date = inputFormat.parse(this)

            // Extract hours and minutes
            val hours = date.hours.toLong()
            val minutes = date.minutes.toLong()

            // Build the formatted string based on hours and minutes
            formattedDuration = if (hours > 0) {
                String.format(Locale.getDefault(), "%d hour %d minute", hours, minutes)
            } else {
                String.format(Locale.getDefault(), "%d minute", minutes)
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return formattedDuration ?: ""
}


fun Context.getMyDeviceId(): String {
    return Settings.Secure.getString(
        this.contentResolver, Settings.Secure.ANDROID_ID
    )
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}


/***
 * Convert the millisecond to String text
 */
fun Long.convertToText(): String {
    val sec = this / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) {
        "0$minutes"
    } else {
        minutes.toString()
    }
    val secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        seconds.toString()
    }
    return "$minutesString:$secondsString"
}


fun Modifier.dashedBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)

@Composable
fun DashedBorder(padding: PaddingValues = PaddingValues(24.sdp)) {
    // Create a path effect for dashed lines
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {

        // Create a Canvas to draw on
        Canvas(
            modifier = Modifier
                .width(30.sdp)
                .height(1.sdp)
        ) {
            // Draw a line on the canvas and apply the path effect for dashed line appearance
            drawLine(
                color = Color.White,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect,
                strokeWidth = 5f
            )
        }
    }
}


fun Activity.getPathFromUris(list: List<Uri>): ArrayList<String> {
    val imagesList = ArrayList<String>()
    list.forEach { selectedImage ->
        if ("content" == selectedImage.scheme) {
            val cursor: Cursor? =
                contentResolver.query(
                    selectedImage,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null,
                    null,
                    null
                )
            cursor?.moveToFirst()
            imagesList.add(
                cursor?.getString(
                    0
                ) ?: ""
            )
            cursor?.close()
        } else {
            imagesList.add(selectedImage.path ?: "")
        }
    }
    return imagesList
}

fun Bitmap.compressMyImage(cacheDir: File, f_name: String): File? {
    val f = File(cacheDir, "user$f_name.jpg")
    f.createNewFile()
    ByteArrayOutputStream().use { stream ->
        compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val bArray = stream.toByteArray()
        FileOutputStream(f).use { os -> os.write(bArray) }
    }//stream
    return f
}


fun Activity.createImageFile_(): File? {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val sd_main = File(Environment.getExternalStorageDirectory(), "/Pictures")
    var success = true
    if (!sd_main.exists())
        success = sd_main.mkdir()

    if (success) {
        val sd = File(imageFileName)

        if (!sd.exists())
            success = sd.mkdir()

        if (success) {
            // directory exists or already created
            val dest = File(sd, imageFileName)

        }
        return sd
    } else {
        // directory creation is not successful
        return null
    }
}


private fun String.parseBold(): AnnotatedString {
    val parts = this.split("<b>", "</b>")
    return buildAnnotatedString {
        var bold = false
        for (part in parts) {
            if (bold) {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(part)
                }
            } else {
                append(part)
            }
            bold = !bold
        }
    }
}

fun Long.toDate(productId: String): Date {
    val date = Date(this)
    val cal = Calendar.getInstance()
    cal.time = date
    return when (productId) {
        "bd_one_month_sub_new" -> {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
            cal.time
        }

        "bd_half_yearly_sub" -> {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6)
            cal.time
        }

        "bd_yearly_sub_new" -> {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1)
            cal.time
        }

        else -> Date()
    }
}

//fun Context.getNotificationIcon(): Int {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        R.drawable.ic_notification
//    } else {
//        R.drawable.ic_notification_white_bg
//    }
//}


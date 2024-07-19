package com.toastdialogue.alerter.utils

import android.os.Build
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import com.toastdialogue.alerter.Alert

fun Alert.getDimenPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

@RequiresApi(Build.VERSION_CODES.P)
fun Alert.notchHeight() = rootWindowInsets?.displayCutout?.safeInsetTop
        ?: 0


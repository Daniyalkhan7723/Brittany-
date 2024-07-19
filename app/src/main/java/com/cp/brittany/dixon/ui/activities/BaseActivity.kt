package com.cp.brittany.dixon.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.core.view.*

open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

    }

    fun handleStatusBar(container: View) {
        //set fullScreen (draw under statusBar and NavigationBar )
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        container.setOnApplyWindowInsetsListener { view, insets ->
            val navigationBarHeight = WindowInsetsCompat.toWindowInsetsCompat(insets)
                .getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            view.updatePadding(bottom = navigationBarHeight)
            WindowInsetsCompat.CONSUMED.toWindowInsets()!!
        }

//        //make statusBar content dark
//        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
//            true
//
//        window.statusBarColor = Color.TRANSPARENT
    }

    //    private fun handleStatusBar() {
//        //set fullScreen (draw under statusBar and NavigationBar )
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        container.setOnApplyWindowInsetsListener { view, insets ->
//            val navigationBarHeight = WindowInsetsCompat.toWindowInsetsCompat(insets)
//                .getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//
//            view.updatePadding(bottom = navigationBarHeight)
//            WindowInsetsCompat.CONSUMED.toWindowInsets()
//        }
//
//        //make statusBar content dark
//        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
//            true
//
//        window.statusBarColor = Color.TRANSPARENT
//    }
    fun fullScreenWithStatusBarWhiteIcon() {
        setStatusBarLightText(window, true)
    }

    fun setStatusBarLightText(window: Window, isLight: Boolean) {
        setStatusBarLightTextOldApi(window, isLight)
        setStatusBarLightTextNewApi(window, isLight)
    }

    private fun setStatusBarLightTextOldApi(window: Window, isLight: Boolean) {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            if (isLight) {
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() or View.SYSTEM_UI_FLAG_IMMERSIVE
            } else {
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        getWindow().statusBarColor = Color.TRANSPARENT

    }

    private fun setStatusBarLightTextNewApi(window: Window, isLightText: Boolean) {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            // Light text == dark status bar
            isAppearanceLightStatusBars = !isLightText
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.navigationBars())
        }
        getWindow().statusBarColor = Color.TRANSPARENT
    }

    fun makeStatusBarTransparent() {
        setStatusBarLightText(window, false)
    }
}
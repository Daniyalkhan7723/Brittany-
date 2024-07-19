package com.cp.brittany.dixon.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = Dark,
    onPrimary = Dark600,
    primaryContainer = Dark700,
    onPrimaryContainer = Dark800,
    inversePrimary = Dark900,
    secondary = Gold,
    onSecondary = Pink,
    secondaryContainer = Light100,
    onSecondaryContainer = Light200,
    tertiary = Light300,
    onTertiary = Light400,
    tertiaryContainer = White0,
    onTertiaryContainer = White,
    background = Default,
    onBackground = PureWhite,
    surface = PureBlack,
    onSurface = ColorViewMore,
    onError = ErrorColor,
    error = PremiumBannerColor,
    outline = SuccessColor,
    outlineVariant = dividerColor,
    surfaceContainer = greenColor,
    onSurfaceVariant = mineShaft,

    )

private val LightColorScheme = lightColorScheme(
    primary = Dark,
    onPrimary = Dark600,
    primaryContainer = Dark700,
    onPrimaryContainer = Dark800,
    inversePrimary = Dark900,
    secondary = Gold,
    onSecondary = Pink,
    secondaryContainer = Light100,
    onSecondaryContainer = Light200,
    tertiary = Light300,
    onTertiary = Light400,
    tertiaryContainer = White0,
    onTertiaryContainer = White,
    background = Default,
    onBackground = PureWhite,
    onError = ErrorColor,
    surface = PureBlack,
    onSurface = ColorViewMore,
    error = PremiumBannerColor,
    outline = SuccessColor,
    outlineVariant = dividerColor,
    surfaceContainer = greenColor,
    onSurfaceVariant = mineShaft,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun BrittanyDixonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//

//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
package com.cp.brittany.dixon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.cp.brittany.dixon.R

// Set of Material typography styles to start with

object AppFont {
    val MyCustomFont = FontFamily(
        listOf(
            Font(R.font.inter_regular),
            Font(R.font.inter_medium, FontWeight.Medium),
            Font(R.font.inter_bold, FontWeight.Bold),
            Font(R.font.inter_light, FontWeight.Light),
            Font(R.font.inter_thin, FontWeight.Thin),
        )
    )
}

private val defaultTypography = Typography()

val Typography = Typography(

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.MyCustomFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.MyCustomFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.MyCustomFont),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.MyCustomFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.MyCustomFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.MyCustomFont),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.MyCustomFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.MyCustomFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.MyCustomFont),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.MyCustomFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.MyCustomFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.MyCustomFont)

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
@file:OptIn(ExperimentalMaterial3Api::class)

package com.cp.brittany.dixon.ui.components

import android.content.Context
import android.content.ContextWrapper
import android.text.Spanned
import android.text.util.Linkify
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.max
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.ui.theme.Primary
import com.cp.brittany.dixon.ui.theme.componentShapes
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.hideSystemUi
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.toastdialogue.alerter.Alerter


@Composable
fun NormalTextComponent(
    value: String,
    textSize: TextUnit,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value, modifier = modifier.fillMaxWidth(), style = TextStyle(
            fontSize = textSize,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = AppFont.MyCustomFont
        ), color = textColor, textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String, textSize: TextUnit, modifier: Modifier = Modifier) {
    Text(
        text = value, modifier = modifier, style = TextStyle(
            fontSize = textSize, fontWeight = FontWeight.Medium, fontFamily = AppFont.MyCustomFont
        ), color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center
    )
}


@Composable
fun NormalTextComponentWithoutFullWidth(
    value: String,
    textSize: TextUnit,
    fontStyle: FontStyle,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value, modifier = modifier, style = TextStyle(
            fontSize = textSize,
            fontWeight = FontWeight.Normal,
            fontStyle = fontStyle,
            fontFamily = AppFont.MyCustomFont
        ), color = textColor
    )
}

@Composable
fun HeadingTextComponentWithoutFullWidth(
    value: String,
    textSize: TextUnit,
    textColor: Color,
    modifier: Modifier = Modifier,

    ) {
    Text(
        text = value, modifier = modifier, style = TextStyle(
            fontSize = textSize, fontWeight = FontWeight.Medium, fontFamily = AppFont.MyCustomFont
        ), color = textColor, textAlign = TextAlign.Center
    )
}


@Composable
fun AutoResizingText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    targetTextSize: TextUnit,
    maxLines: Int = Int.MAX_VALUE
) {
    val textSize = remember {
        mutableStateOf(targetTextSize)
    }

    Text(style = TextStyle(
        fontSize = textSize.value,
        fontWeight = FontWeight.Medium,
        fontFamily = AppFont.MyCustomFont,
    ),
        textAlign = TextAlign.Center,
        modifier = modifier,
        text = text,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            var lineIndex = textLayoutResult.lineCount - 1
            if (textLayoutResult.isLineEllipsized(lineIndex)) {
                textSize.value = textSize.value * 0.9f
            }

        })

}


@Composable
fun BoldTextComponent(
    value: String, textSize: TextUnit, textColor: Color, modifier: Modifier = Modifier
) {
    Text(
        text = value, maxLines = 1, modifier = modifier, style = TextStyle(
            fontSize = textSize, fontWeight = FontWeight.Bold, fontFamily = AppFont.MyCustomFont
        ), color = textColor, textAlign = TextAlign.Center
    )
}

@Composable
fun ItalicTextComponent(
    value: String, textSize: TextUnit, textColor: Color, modifier: Modifier = Modifier
) {
    Text(
        text = value, maxLines = 1, modifier = modifier, style = TextStyle(
            fontSize = textSize,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            fontFamily = AppFont.MyCustomFont
        ), color = textColor, textAlign = TextAlign.Center
    )
}

@Composable
fun ItalicBoldTextComponent(
    value: String, textSize: TextUnit, textColor: Color, modifier: Modifier = Modifier
) {
    Text(
        text = value, maxLines = 1, modifier = modifier, style = TextStyle(
            fontSize = textSize,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFont.MyCustomFont
        ), color = textColor, textAlign = TextAlign.Center
    )
}


//@Composable
//fun LinearProgress(modifier: Modifier = Modifier) {
//    LinearProgressIndicator(
//        progress = 0.6f,
//        modifier = modifier,
//        color = colorResource(id = R.color.primary_pink),
//        strokeCap = StrokeCap.Butt,
//    )
//}

@Composable
fun showProgress(progress: Int, modifier: Modifier = Modifier) {
    val progressFactor by remember(progress) {
        mutableFloatStateOf(progress * 0.005f)
    }

    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            contentPadding = PaddingValues(1.sdp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(color = MaterialTheme.colorScheme.onSecondary),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent, disabledContainerColor = Color.Transparent
            )
        ) {

        }
    }
}


@Composable
fun AreShapeOnBorderCenterSurface(
    cornerRadius: Dp, centerCircleRadius: Dp, content: @Composable () -> Unit
) {

    val density = LocalDensity.current
    val cornerRadiusPx = density.run {
        cornerRadius.toPx()
    }
    val centerCircleRadiusPx = density.run {
        centerCircleRadius.toPx()
    }

    Box(contentAlignment = Alignment.TopCenter) {
        val diameter = 60.sdp

        IconButton(
            onClick = {},
            modifier = Modifier
                .offset(y = -diameter / 10)
                .size(diameter)
                .align(Alignment.TopCenter)
                .background(
                    color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(50.sdp)
                ),


            ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Add icon",
                tint = Color.White
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 25.sdp)
                .wrapContentHeight(),
            shadowElevation = 5.sdp,
            color = Color.White,
            shape = GenericShape { size: Size, _: LayoutDirection ->
                buildCustomPath(size, cornerRadiusPx, centerCircleRadiusPx)
            },
//            border = BorderStr  oke(1.dp, Color.Gray.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = centerCircleRadius)
            ) {
                content()
            }
        }
    }

}


fun Path.buildCustomPath(size: Size, cornerRadius: Float, centerCircleRadius: Float) {
    val width = size.width
    val height = size.height

    // 顶部简化计算的
    val topHalfMoveLength = (width - 2 * cornerRadius - 2 * centerCircleRadius) / 2

    // 单位长度
    val smallCubeLength = centerCircleRadius / 20

    val firstCubicPoint1 = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 8 * smallCubeLength, y = 1 * smallCubeLength
    )
    val firstCubicPoint2 = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 4 * smallCubeLength, y = 16 * smallCubeLength
    )
    val firstCubicTarget = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + centerCircleRadius, y = 16 * smallCubeLength
    )
    val secondCubicPoint1 = Offset(
        x = width - firstCubicPoint2.x, y = firstCubicPoint2.y
    )
    val secondCubicPoint2 = Offset(
        x = width - firstCubicPoint1.x, y = firstCubicPoint1.y
    )
    val secondCubicTarget = Offset(
        x = 1 * cornerRadius + topHalfMoveLength + 2 * centerCircleRadius, y = 0f
    )


    moveTo(cornerRadius, 0f)
    lineTo(cornerRadius + topHalfMoveLength, 0f)

    cubicTo(
        x1 = firstCubicPoint1.x,
        y1 = firstCubicPoint1.y,
        x2 = firstCubicPoint2.x,
        y2 = firstCubicPoint2.y,
        x3 = firstCubicTarget.x,
        y3 = firstCubicTarget.y,
    )
    cubicTo(
        x1 = secondCubicPoint1.x,
        y1 = secondCubicPoint1.y,
        x2 = secondCubicPoint2.x,
        y2 = secondCubicPoint2.y,
        x3 = secondCubicTarget.x,
        y3 = secondCubicTarget.y,
    )

    lineTo(width - cornerRadius, 0f)
    arcTo(
        rect = Rect(
            topLeft = Offset(x = width - 2 * cornerRadius, y = 0f),
            bottomRight = Offset(x = width, y = 2 * cornerRadius)
        ), startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    lineTo(width, height - 1f)
    arcTo(
        rect = Rect(
            topLeft = Offset(x = width - 2 * 1f, y = height - 2 * 1f),
            bottomRight = Offset(x = width, y = height)
        ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
    )

    lineTo(0f + 1f, height)
    arcTo(
        rect = Rect(
            topLeft = Offset(x = 0f, y = height - 2 * 1f),
            bottomRight = Offset(x = 2 * 1f, y = height)
        ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    lineTo(0f, 1f)
    arcTo(
        rect = Rect(
            topLeft = Offset.Zero, bottomRight = Offset(x = 2 * 1f, y = 2 * 1f)
        ), startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
    )
    close()
}


@Composable
fun TextFieldComponent(
    labelValue: String,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false,
    errorText: String,
    stringValue: String = "",
    unFocusColor: Color,
    focusColor: Color,
    modifier: Modifier = Modifier,
    errorModifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: ImeAction = ImeAction.Next,

    ) {

    val textValue = remember {
        mutableStateOf(stringValue)
    }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    )

    val localFocusManager = LocalFocusManager.current

    Column {

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(53.sdp)
                    .clip(componentShapes.small),
                label = { Text(text = labelValue) },

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = focusColor,
                    unfocusedBorderColor = unFocusColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                ),

                keyboardOptions = KeyboardOptions(
                    imeAction = keyboardActions, keyboardType = keyboardType
                ),
                singleLine = true,
                maxLines = 1,
                value = textValue.value,
                onValueChange = {
                    textValue.value = it
                    onTextChanged(it)
                },
                shape = RoundedCornerShape(30.sdp),

                )
        }

        if (errorStatus) {
            ItalicTextComponent(
                value = errorText,
                textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.error,
                modifier = errorModifier
            )
        }

    }

}


@Composable
fun SearchedComponent(
    labelValue: String,
    onQueryChanged: (String) -> Unit,
    query: String = "",
    keyboardActions: ImeAction = ImeAction.Done,
    modifier: Modifier = Modifier,
    layoutModifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackPress: () -> Unit
) {

    val backPressClick by remember {
        mutableStateOf(Modifier.clickable {
            onBackPress()
        })
    }


    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    )
    Row(modifier = layoutModifier) {
        if (showBackButton) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.sdp)
                    .then(backPressClick)
                    .align(Alignment.CenterVertically)
                    .size(22.sdp),
            )
        }


        Spacer(modifier = Modifier.width(5.sdp))
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.sdp)
                    .border(
                        width = 1.sdp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(48.sdp)
                    )

                    // To make the ripple round
                    .clip(shape = RoundedCornerShape(percent = 50)),
                contentAlignment = Alignment.Center
            ) {
                val focusManager = LocalFocusManager.current

                TextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(53.sdp)
                        .clip(componentShapes.small),
                    placeholder = {

                        Text(
                            text = labelValue,
                            modifier = modifier,
                            style = TextStyle(
                                fontSize = 12.ssp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = AppFont.MyCustomFont
                            ),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            textAlign = TextAlign.Center
                        )

//                        Text(text = labelValue)


                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondaryContainer,
                        cursorColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = keyboardActions,
                        autoCorrect = true,
                    ),

                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        },
                    ),
                    singleLine = true,

                    maxLines = 1,
                    value = query,
                    onValueChange = {
                        onQueryChanged(it)
                    },
                    shape = RoundedCornerShape(30.sdp),
                    trailingIcon = {
                        val iconImage = painterResource(id = R.drawable.ic_close)
                        IconButton(onClick = {
                            onQueryChanged("")
                        }) {

                            Image(
                                painter = iconImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 5.sdp)
                                    .size(21.sdp)
                            )
                        }

                    },
                )

            }
        }
    }

}


@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    errorText: String,
    stringValue: String = "",
    unFocusColor: Color,
    focusColor: Color,
    keyboardActions: ImeAction = ImeAction.Next,
    modifier: Modifier = Modifier
) {

    val localFocusManager = LocalFocusManager.current
    val password = remember {
        mutableStateOf(stringValue)
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Column {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(53.sdp)
                    .clip(componentShapes.small),
                label = { Text(text = labelValue) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = focusColor,
                    unfocusedBorderColor = unFocusColor,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                ),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = keyboardActions
                ),
                singleLine = true,

//                keyboardActions = KeyboardActions {
//                    localFocusManager.clearFocus()
//                },

                maxLines = 1,
                value = password.value,
                onValueChange = {
                    password.value = it
                    onTextSelected(it)
                },
                shape = RoundedCornerShape(30.sdp),
                trailingIcon = {
                    val iconImage = if (passwordVisible.value) {
                        painterResource(id = R.drawable.ic_eye_hide)
                    } else {
                        painterResource(id = R.drawable.ic_eye_unhide)
                    }

                    val description = if (passwordVisible.value) {
                        stringResource(id = R.string.hide_password)
                    } else {
                        stringResource(id = R.string.show_password)
                    }

                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {

                        Image(
                            painter = iconImage,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.sdp)
                                .size(18.sdp)
                        )
                    }

                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            )

            if (errorStatus) {
                ItalicTextComponent(
                    value = errorText,
                    textSize = 12.ssp,
                    textColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 4.sdp, top = 5.sdp)
                )
            }


        }
    }

}


@Composable
fun showToast(title: String, isSuccess: Boolean) {
    if (isSuccess) {
        AlertDialogue(
            stringResource(R.string.success), title, MaterialTheme.colorScheme.outline, true
        )
    } else {
        AlertDialogue(
            stringResource(R.string.error), title, MaterialTheme.colorScheme.error, true
        )
    }
}


@Composable
fun AlertDialogue(
    title: String, description: String, backgroundColor: Color, showAlertDialogue: Boolean
) {
    var showAlert = showAlertDialogue
    Alerter(
        isShown = showAlert, onChanged = { showAlert = it }, backgroundColor = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

//            Icon(
//                imageVector = Icons.Rounded.Notifications, contentDescription = "",
//                tint = Color.White, modifier = Modifier
//                    .padding(start = 12.dp)
//                    .iconPulse()
//            )

            Column(modifier = Modifier.padding(horizontal = 12.sdp)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.ssp
                )
                Text(text = description, color = Color.White, fontSize = 14.ssp)

            }
        }
    }
}

@Composable
fun ButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    isEnabled: Boolean = true,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    ) {
    Button(
        modifier = modifier,
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = buttonColors(Color.Transparent),
        shape = RoundedCornerShape(48.sdp),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(45.sdp)
                .background(
                    color = buttonColor, shape = RoundedCornerShape(48.sdp)
                ), contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.sdp),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            } else {

                HeadingTextComponentWithoutFullWidth(
                    value = value,
                    textSize = 13.ssp,
                    textColor = textColor,
                    modifier = Modifier.heightIn(),
                )
            }


        }

    }
}


@Composable
fun ButtonComponentNoFullWidth(
    value: String,
    onButtonClicked: () -> Unit,
    isEnabled: Boolean = true,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    ) {
    Button(
        modifier = modifier,
        onClick = {
            onButtonClicked.invoke()
        },
//        contentPadding = PaddingValues(),
        colors = buttonColors(Color.Transparent),
        shape = RoundedCornerShape(48.sdp),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .width(240.sdp)
                .heightIn(45.sdp)
                .background(
                    color = buttonColor, shape = RoundedCornerShape(48.sdp)
                ), contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.sdp),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            } else {

                HeadingTextComponentWithoutFullWidth(
                    value = value,
                    textSize = 13.ssp,
                    textColor = textColor,
                    modifier = Modifier.heightIn(),
                )
            }


        }

    }
}


@Composable
fun BorderButtonRound(
    value: String,
    onButtonClicked: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    shapeRadius: Int,
    borderRadius: Int,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    borderColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            onButtonClicked()
        })
    }
    Box(
        modifier = modifier
            .height(45.sdp)
            .border(
                width = 1.sdp,
                color = borderColor,
                shape = RoundedCornerShape(percent = borderRadius)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(shapeRadius.sdp)
            )

            // To make the ripple round
            .clip(shape = RoundedCornerShape(percent = borderRadius))
            .then(click),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 12.ssp,
            modifier = Modifier.padding(paddingValues),
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontFamily = AppFont.MyCustomFont
        )
    }
}


@Composable
fun SocialButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    imageSize: Dp,
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            onClick()
        })
    }
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(percent = 50))
            .border(
                width = 1.sdp,
                color = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(percent = 50)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(48.sdp)
            )
            .then(click)
            .height(40.sdp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(imageSize)
        )
        Spacer(modifier = Modifier.width(5.sdp))

        Text(
            text = text,
            fontSize = 11.ssp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,
            fontFamily = AppFont.MyCustomFont
        )
    }

}

@Composable
fun SocialLoginButton(
    value: String,
    onButtonClicked: () -> Unit,
    imageProp: Int,
    imageSizeDp: Dp,

    ) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            onButtonClicked()
        })
    }
    Box(
        modifier = Modifier
            .widthIn(50.sdp)
            .heightIn(35.sdp)
            .border(
                width = 1.sdp,
                color = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(percent = 50)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(48.sdp)
            )

            // To make the ripple round
            .clip(shape = RoundedCornerShape(percent = 50))
            .then(click),
        contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier.padding(horizontal = 23.sdp, vertical = 5.sdp)) {
            Image(
                painter = painterResource(id = imageProp),
                contentDescription = "logoGlampian",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(imageSizeDp)
//                    .width(15.sdp)
//                    .height(15.sdp)

            )
            Spacer(modifier = Modifier.width(10.sdp))
            Text(
                text = value,
                fontSize = 11.ssp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontFamily = AppFont.MyCustomFont
            )
        }

    }
}

@Composable
fun DividerTextComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onTertiary,
            thickness = 1.sdp
        )

        Text(
            modifier = Modifier.padding(8.sdp),
            text = stringResource(R.string.or),
            fontSize = 15.ssp,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onTertiary,
            thickness = 1.sdp
        )
    }
}


@Composable
fun ClickableLoginTextComponent(
    tryingToLogin: Boolean = true,
    onTextSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialText =
        if (tryingToLogin) "Already a Glampions Member? " else "Don’t have an account? "
    val loginText = if (tryingToLogin) "Login" else "Sign up"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = modifier,
        style = TextStyle(
            fontSize = 12.ssp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            fontFamily = AppFont.MyCustomFont,
            color = MaterialTheme.colorScheme.onBackground
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if (span.item == loginText) {
                    onTextSelected(span.item)
                }
            }

        },
    )
}


@Composable
fun ClickableTextComposable(
    description: String,
    onTextSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialText = description
    val clickableText = " Read More"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Bold
            )
        ) {
            pushStringAnnotation(tag = clickableText, annotation = clickableText)
            append(clickableText)
        }
    }

    ClickableText(
        modifier = modifier,
        style = TextStyle(
            fontSize = 12.ssp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = AppFont.MyCustomFont,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if (span.item == clickableText) {
                    onTextSelected(span.item)
                }
            }

        },
    )
}

@Composable
fun UnderLinedTextComponent(
    value: String, onButtonClicked: () -> Unit, modifier: Modifier = Modifier
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
            onButtonClicked()
        })
    }
    Text(
        text = AnnotatedString(value),
        modifier = modifier
            .heightIn(min = 40.sdp)
            .then(click),
        style = TextStyle(
            fontSize = 16.ssp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = AppFont.MyCustomFont,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        ),
    )

}

/**
 * Find a [androidx.activity.ComponentActivity] from the current context.
 * By default Jetpack Compose project uses ComponentActivity for MainActivity,
 * It is a parent of [androidx.fragment.app.FragmentActivity] or [AppCompatActivity]
 */
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


@Composable
fun UpsideGlassGradient(
    modifier: Modifier = Modifier, startY: Float, color: Color, posterImage: Painter
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(painter = posterImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                // Workaround to enable alpha compositing
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent, Color.Black, Color.Black
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors, startY = startY),
                        blendMode = BlendMode.DstIn
                    )
                }
                .blur(20.sdp))
        Box(
            modifier = modifier
                .fillMaxSize()
                .alpha(0.9f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, color, color
                        ), startY = startY
                    )
                )
        )
    }

}

@Composable
fun HideSystemBars() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = context.findActivity()?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
}


@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    context.hideSystemUi()
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}


@Composable
fun DashedBorder(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(7f, 7f), 0f)

    Box(
        modifier = modifier
            .padding(top = 25.sdp, end = 5.sdp)
            .rotate(90f)
    ) {
        // Create a Canvas to draw on
        Canvas(
            modifier = Modifier
                .width(50.sdp)
                .height(1.sdp)

        ) {
            // Draw a line on the canvas and apply the path effect for dashed line appearance
            drawLine(
                color = Color(0xFF54575B),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect,
                strokeWidth = 5f
            )
        }
    }
}

@Composable
fun NoDataFound(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_data_found),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        HeadingTextComponentWithoutFullWidth(
            value = stringResource(R.string.no_data_found),
            textSize = 14.ssp,
            textColor = Color(0xFFB6BFCC),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.sdp)

        )

    }

}


@Composable
fun htmlText(
    context: Context,
    spannedText: Spanned,
    textStyle: TextStyle,
) {
    AndroidView(
        modifier = Modifier.padding(top = 15.sdp),
        factory = {
            val spacingReady =
                kotlin.math.max(
                    textStyle.lineHeight.value - textStyle.fontSize.value - Constants.SPACING_FIX,
                    0f
                )
            val extraSpacing = spToPx(spacingReady.toInt(), context)
            val fontResId = R.font.inter_regular
            val font = ResourcesCompat.getFont(context, fontResId)


            TextView(it).apply {
                typeface = font
                autoLinkMask = Linkify.WEB_URLS
                setTextColor(Color(0xFF9D9EA1).toArgb())
                textSize = textStyle.fontSize.value
                setLineSpacing(extraSpacing, 1f)
            }
        },

        update = {
            it.text = spannedText
        }
    )
}

fun spToPx(sp: Int, context: Context): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )


@Composable
fun DisposableEffectWithLifecycle(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onDestroy: () -> Unit = {},
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnDestroy by rememberUpdatedState(onDestroy)

    DisposableEffect(lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate()
                Lifecycle.Event.ON_START -> currentOnStart()
                Lifecycle.Event.ON_PAUSE -> currentOnPause()
                Lifecycle.Event.ON_RESUME -> currentOnResume()
                Lifecycle.Event.ON_STOP -> currentOnStop()
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
        }
    }
}




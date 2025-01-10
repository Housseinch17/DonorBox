@file:OptIn(ExperimentalLayoutApi::class)

package com.example.donorbox.presentation.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.donorbox.R
import com.example.donorbox.presentation.theme.BodyTypography
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography


@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.3f),  // Light grey
        color.copy(alpha = 0.5f),  // Slightly more opaque
        color.copy(alpha = 0.7f),  // Darker grey
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

@Composable
fun SharedScreen(modifier: Modifier = Modifier.fillMaxSize(), content: @Composable () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Semi-transparent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NewBlue.copy(alpha = 0.7f),
                            NewBlue.copy(alpha = 0.5f),
                            NewBlue.copy(alpha = 0.3f),
                            NewWhite.copy(alpha = 0.3f),
                            NewWhite.copy(alpha = 0.5f),
                            NewWhite.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        content()
    }
}

@Composable
fun ShowDialog(
    modifier: Modifier,
    showDialog: Boolean,
    title: String,
    isProgressBar: Boolean,
    description: @Composable () -> Unit,
    confirmText: String,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = confirmButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NewBlue
                    )
                ) {
                    Text(
                        text = confirmText, color = NewWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Button(
                    enabled = !isProgressBar,
                    onClick = onDismissButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NewGray
                    )
                ) {
                    Text(
                        stringResource(R.string.dismiss),
                        color = NewWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            title = {
                Text(
                    text = title,
                    style = TitleTypography
                )
            },
            text = {
                description()
            })
    }
}


@Composable
fun TrailingIcon(imageVector: ImageVector, onIconClick: () -> Unit) {
    IconButton(onClick = onIconClick, modifier = Modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.visibility),
        )
    }
}

@Composable
fun AccountTextButton(
    modifier: Modifier, text: String, textButtonEnabled: Boolean, onSignUpClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onSignUpClick,
        enabled = textButtonEnabled,
    ) {
        Text(
            text, style = BodyTypography.copy(
                color = if (textButtonEnabled) NewBlue else NewGray,
            )
        )
    }
}

@Composable
fun AccountButton(
    modifier: Modifier,
    text: String,
    containerColor: Color = NewGray,
    buttonEnabled: Boolean,
    onLogInClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onLogInClick,
        enabled = buttonEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = text,
            style = BodyTypography.copy(
                color = NewWhite,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun EmailAndPassword(
    modifier: Modifier,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit,
) {
    //imeAction = ImeAction.Done here we say that when we click on the button on keyboard
    //that is in bottom right its like the enter button or something
    //it will perform the onDone function that we will implement in keyboardAction
    Column(modifier) {
        TextFieldInput(
            modifier = modifier,
            label = stringResource(R.string.email),
            value = emailValue,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            onValueChange = { newEmail ->
                onEmailChange(newEmail)
            },
        )
        Spacer(Modifier.height(16.dp))
        PasswordTextField(
            modifier = modifier,
            label = stringResource(R.string.password),
            value = passwordValue,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = getPasswordVisualTransformation(showPassword),
            onValueChange = { newPassword ->
                onPasswordChange(newPassword)
            },
            trailingIcon = trailingIcon
        )
    }
}


@Composable
fun PasswordTextField(
    modifier: Modifier, label: String,
    value: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation, onValueChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //current focus manager if focused or not
    val focusManager = LocalFocusManager.current

    //check if keyboard is open or closed
    val isImeVisible = WindowInsets.isImeVisible

    //when keyboard closed clear focus (show unfocusedContainerColor)
    if (!isImeVisible) {
        focusManager.clearFocus()
    }

    TextField(
        textStyle = TextStyle.Default.copy(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = NewBlue,
            unfocusedIndicatorColor = NewBlue,
            focusedTextColor = NewBlue,
            unfocusedTextColor = NewGray,
            focusedLeadingIconColor = NewBlue,
            unfocusedLeadingIconColor = NewGray,
            unfocusedLabelColor = NewGray,
            focusedLabelColor = NewBlue,
            focusedTrailingIconColor = NewBlue,
            unfocusedTrailingIconColor = NewGray
        ),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = label, fontSize = 14.sp)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock, contentDescription = null
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()

            }),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,

        )
}

@Composable
fun TextFieldInput(
    modifier: Modifier, label: String,
    value: String,
    imageVector: ImageVector = Icons.Filled.Email,
    keyboardOptions: KeyboardOptions, onValueChange: (String) -> Unit,
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //current focus manager if focused or not
    val focusManager = LocalFocusManager.current

    //check if keyboard is open or closed
    val isImeVisible = WindowInsets.isImeVisible

    //when keyboard closed clear focus (show unfocusedContainerColor)
    if (!isImeVisible) {
        focusManager.clearFocus()
    }


    TextField(
        textStyle = TextStyle.Default.copy(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = NewBlue,
            unfocusedIndicatorColor = NewBlue,
            focusedTextColor = NewBlue,
            unfocusedTextColor = NewGray,
            focusedLeadingIconColor = NewBlue,
            unfocusedLeadingIconColor = NewGray,
            unfocusedLabelColor = NewGray,
            focusedPlaceholderColor = NewBlue,
            unfocusedPlaceholderColor = NewGray
        ),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        placeholder = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }),
    )
}


fun getPasswordVisualTransformation(showValue: Boolean): VisualTransformation {
    return if (showValue) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
}

@Composable
fun DonorBoxImage(modifier: Modifier, imageUrl: String?) {
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).crossfade(true)
                .placeholder(R.drawable.loading)
                .error(R.drawable.connectionerror)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }
}

@Composable
fun SettingsShowDialog(
    modifier: Modifier,
    showDialog: Boolean,
    title: String,
    isProgressBar: Boolean,
    description: @Composable () -> Unit,
    confirmText: String,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = confirmButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NewBlue
                    )
                ) {
                    Text(
                        text = confirmText, color = NewWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Button(
                    enabled = !isProgressBar,
                    onClick = onDismissButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NewGray
                    )
                ) {
                    Text(
                        stringResource(R.string.dismiss),
                        color = NewWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            title = {
                Text(
                    text = title,
                    style = TitleTypography
                )
            },
            text = {
                description()
            })
    }
}


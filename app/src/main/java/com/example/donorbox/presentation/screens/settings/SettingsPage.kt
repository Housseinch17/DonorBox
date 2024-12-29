package com.example.donorbox.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donorbox.R
import com.example.donorbox.presentation.theme.BodyTypography
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.PasswordTextField
import com.example.donorbox.presentation.util.SettingsShowDialog
import com.example.donorbox.presentation.util.TrailingIcon
import com.example.donorbox.presentation.util.getPasswordVisualTransformation

@Composable
fun SettingsPage(
    modifier: Modifier,
    newPasswordValue: String,
    newPasswordValueChange: (String) -> Unit,
    confirmPasswordValue: String,
    confirmPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    confirmImageVector: ImageVector,
    confirmOnIconClick: () -> Unit,
    showPassword: Boolean,
    confirmShowPassword: Boolean,
    showText: Boolean,
    confirmShowText: Boolean,
    onSignOut: () -> Unit,
    onPasswordChange: (newPassword: String, confirmPassword: String) -> Unit,
    onResetPassword: () -> Unit,
    resetShowDialog: Boolean,
    resetPassword: () -> Unit,
    resetDismiss: () -> Unit,
    resetIsLoading: Boolean,
    signOutShowDialog: Boolean,
    signOutConfirm: () -> Unit,
    signOutDismiss: () -> Unit,
    signOutIsLoading: Boolean,
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.donate),
            contentDescription = stringResource(R.string.background_image),
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )
        if (resetShowDialog) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsShowDialog(
                    modifier = Modifier,
                    showDialog = resetShowDialog,
                    title = stringResource(R.string.reset_password),
                    isProgressBar = resetIsLoading,
                    description = {
                        if (!resetIsLoading) {
                            Text(
                                text = stringResource(R.string.are_you____reset),
                                style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(80.dp),
                                    color = Color.Gray,
                                    strokeWidth = 2.dp
                                )
                            }

                        }
                    },
                    confirmText = stringResource(R.string.reset_password),
                    confirmButton = resetPassword,
                    onDismissButton = resetDismiss
                )
            }
        } else if (signOutShowDialog) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsShowDialog(
                    modifier = Modifier,
                    showDialog = signOutShowDialog,
                    title = stringResource(R.string.sign_out),
                    isProgressBar = signOutIsLoading,
                    description =
                    {
                        if (!signOutIsLoading) {
                            Text(
                                text = stringResource(R.string.are_you____signOut),
                                style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(80.dp),
                                    color = Color.Gray,
                                    strokeWidth = 10.dp
                                )
                            }

                        }
                    },
                    confirmText = stringResource(R.string.sign_out),
                    confirmButton = signOutConfirm,
                    onDismissButton = signOutDismiss
                )
            }

        }

        Column(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.new_password),
                style = TitleTypography.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(14.dp))
            ChangePasswordField(
                newPasswordValue = newPasswordValue,
                newPasswordValueChange = newPasswordValueChange,
                showPassword = showPassword,
                imageVector = imageVector,
                onIconClick = onIconClick
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (showText) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.your_password_must_be_at_least) + 6 + stringResource(
                        R.string.characters
                    ),
                    style = BodyTypography.copy(
                        color = Color.Red, fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.confirm_password),
                style = TitleTypography.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(14.dp))
            ChangePasswordField(
                newPasswordValue = confirmPasswordValue,
                newPasswordValueChange = confirmPasswordValueChange,
                showPassword = confirmShowPassword,
                imageVector = confirmImageVector,
                onIconClick = confirmOnIconClick
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (confirmShowText) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.your_password_must_be_at_least) + 6 + stringResource(
                        R.string.characters
                    ),
                    style = BodyTypography.copy(
                        color = Color.Red, fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(onClick = {
                onPasswordChange(newPasswordValue, confirmPasswordValue)
            }) {
                Text(text = stringResource(R.string.change_password))
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onResetPassword) {
                    Text(text = stringResource(R.string.reset_password))
                }
                Button(onClick = onSignOut) {
                    Text(text = stringResource(R.string.sign_out))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

@Composable
fun ChangePasswordField(
    newPasswordValue: String,
    showPassword: Boolean,
    newPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        label = stringResource(R.string.password), value = newPasswordValue,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = getPasswordVisualTransformation(showPassword),
        onValueChange = newPasswordValueChange,
        trailingIcon = {
            TrailingIcon(imageVector = imageVector, onIconClick = onIconClick)
        }
    )
}
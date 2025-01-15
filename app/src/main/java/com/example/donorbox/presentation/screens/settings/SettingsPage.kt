package com.example.donorbox.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donorbox.R
import com.example.donorbox.presentation.screens.authentication.AuthenticationAction
import com.example.donorbox.presentation.screens.authentication.AuthenticationUiState
import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import com.example.donorbox.presentation.screens.authentication.ResetPage
import com.example.donorbox.presentation.screens.authentication.SignOutResponse
import com.example.donorbox.presentation.theme.BodyTypography
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.PasswordTextField
import com.example.donorbox.presentation.util.SettingsShowDialog
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.TrailingIcon
import com.example.donorbox.presentation.util.getPasswordVisualTransformation

@Composable
fun SettingsPage(
    modifier: Modifier,
    settingsUiState: SettingsUiState,
    onActionSettings: (SettingsAction) -> Unit,
    authenticationUiState: AuthenticationUiState,
    onActionAuthentication: (AuthenticationAction) -> Unit,
    textPage: String,
) {
    SharedScreen {
        Box(modifier = modifier) {
            if (authenticationUiState.resetShowDialog) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SettingsShowDialog(
                        modifier = Modifier,
                        showDialog = true,
                        title = stringResource(R.string.reset_password),
                        isProgressBar = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
                        description = {
                            if (authenticationUiState.resetPassword != PasswordChangement.IsLoading) {
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
                                        color = NewGray,
                                        strokeWidth = 2.dp
                                    )
                                }

                            }
                        },
                        confirmText = stringResource(R.string.reset_password),
                        confirmButton = {
                            onActionAuthentication(
                                AuthenticationAction.ResetPassword(
                                    emailValue = "",
                                    resetPage = ResetPage.SettingsPage
                                )
                            )
                        },
                        onDismissButton = {
                            onActionAuthentication(AuthenticationAction.ResetDismiss)
                        }
                    )
                }
            } else if (authenticationUiState.signOutShowDialog) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SettingsShowDialog(
                        modifier = Modifier,
                        showDialog = true,
                        title = stringResource(R.string.sign_out),
                        isProgressBar = authenticationUiState.signOut == SignOutResponse.IsLoading,
                        description =
                        {
                            if (authenticationUiState.signOut != SignOutResponse.IsLoading) {
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
                        confirmButton = {
                            onActionAuthentication(AuthenticationAction.SignOut)
                        },
                        onDismissButton = {
                            onActionAuthentication(AuthenticationAction.ResetHideDialog)
                        }
                    )
                }
            }

            Column(
                modifier = modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    text = textPage,
                    style = TitleTypography.copy(color = NewWhite),
                )
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(0.4f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        PasswordTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.current_password),
                            value = settingsUiState.currentPasswordValue,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = getPasswordVisualTransformation(
                                settingsUiState.currentShowPassword
                            ),
                            onValueChange = { currentPassword ->
                                onActionSettings(
                                    SettingsAction.UpdatePassword(
                                        UpdatePassword.CurrentPassword(
                                            currentPassword
                                        )
                                    )
                                )
                            },
                            trailingIcon = {
                                TrailingIcon(
                                    imageVector = if (settingsUiState.currentShowPassword) {
                                        Icons.Filled.Visibility
                                    } else {
                                        Icons.Filled.VisibilityOff
                                    },
                                    onIconClick = {
                                        onActionSettings(SettingsAction.ShowPassword(ShowPassword.CurrentPassword))
                                    }
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        ChangePasswordField(
                            newPasswordValue = settingsUiState.newPasswordValue,
                            newPasswordValueChange = { newPassword ->
                                onActionSettings(
                                    SettingsAction.UpdatePassword(
                                        UpdatePassword.NewPassword(
                                            newPassword
                                        )
                                    )
                                )
                            },
                            label = stringResource(R.string.new_password),
                            showPassword = settingsUiState.newShowPassword,
                            imageVector = if (settingsUiState.newShowPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            onIconClick = {
                                onActionSettings(SettingsAction.ShowPassword(ShowPassword.NewPassword))
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (settingsUiState.showText) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.your_password_must_be_at_least) + " 6 "+ stringResource(
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
                        Spacer(modifier = Modifier.height(20.dp))
                        ChangePasswordField(
                            newPasswordValue = settingsUiState.confirmNewPasswordValue,
                            newPasswordValueChange = { confirmPassword ->
                                onActionSettings(
                                    SettingsAction.UpdatePassword(
                                        UpdatePassword.ConfirmPassword(
                                            confirmPassword
                                        )
                                    )
                                )
                            },
                            label = stringResource(R.string.confirm_password),
                            showPassword = settingsUiState.confirmShowPassword,
                            imageVector = if (settingsUiState.confirmShowPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            onIconClick = {
                                onActionSettings(SettingsAction.ShowPassword(ShowPassword.ConfirmPassword))
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (settingsUiState.confirmShowText) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.your_password_must_be_at_least) + " 6 " + stringResource(
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
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(0.4f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                onActionSettings(
                                    SettingsAction.OnPasswordChange(
                                        username = authenticationUiState.username!!,
                                        currentPassword = settingsUiState.currentPasswordValue,
                                        newPassword = settingsUiState.newPasswordValue,
                                        confirmPassword = settingsUiState.confirmNewPasswordValue
                                    )
                                )
                            },
                            enabled = !settingsUiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NewBlue,
                                contentColor = NewWhite
                            )
                        ) {
                            Text(text = stringResource(R.string.change_password))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    onActionAuthentication(AuthenticationAction.OnResetPassword)
                                },
                                enabled = !settingsUiState.isLoading,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NewBlue,
                                    contentColor = NewWhite
                                )
                            ) {
                                Text(text = stringResource(R.string.reset_password))
                            }
                            Button(
                                onClick = {
                                    onActionAuthentication(AuthenticationAction.ResetShowDialog)
                                },
                                enabled = !settingsUiState.isLoading,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NewBlue,
                                    contentColor = NewWhite
                                )
                            ) {
                                Text(text = stringResource(R.string.sign_out))
                            }
                        }
                    }
                }

            }
            if (settingsUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center),
                    color = NewBlue,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Composable
fun ChangePasswordField(
    newPasswordValue: String,
    showPassword: Boolean,
    label: String = stringResource(R.string.password),
    newPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        label = label, value = newPasswordValue,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = getPasswordVisualTransformation(showPassword),
        onValueChange = newPasswordValueChange,
        trailingIcon = {
            TrailingIcon(imageVector = imageVector, onIconClick = onIconClick)
        },
    )
}
package com.example.donorbox.presentation.screens.login

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.presentation.screens.authentication.AuthenticationAction
import com.example.donorbox.presentation.screens.authentication.AuthenticationUiState
import com.example.donorbox.presentation.screens.authentication.ResetPage
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.AccountButton
import com.example.donorbox.presentation.util.AccountTextButton
import com.example.donorbox.presentation.util.EmailAndPassword
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.ShimmerEffect
import com.example.donorbox.presentation.util.ShowDialog
import com.example.donorbox.presentation.util.TrailingIcon


@Composable
fun LogInScreen(
    modifier: Modifier,
    textPage: String,
    logInUiState: LogInUiState,
    authenticationUiState: AuthenticationUiState,
    onActionLogIn: (LogInAction) -> Unit,
    onActionAuthentication: (AuthenticationAction) -> Unit,
    onSignUpClick: () -> Unit,
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    SharedScreen {
        if (authenticationUiState.resetShowDialog) {
            Box(
                modifier = modifier
            ) {
                val horizontalScrollState = rememberScrollState()
                ShowDialog(
                    modifier = Modifier,
                    showDialog = true,
                    title = stringResource(R.string.reset_password),
                    isProgressBar = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
                    description = {
                        if (authenticationUiState.resetPassword != PasswordChangement.IsLoading) {
                            Column {
                                Text(
                                    text = stringResource(R.string.are_you____reset),
                                    style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                                )
                                Spacer(Modifier.height(16.dp))
                                TextField(
                                    value = authenticationUiState.resetEmailValue,
                                    onValueChange = { emailValue ->
                                        onActionAuthentication(
                                            AuthenticationAction.OnResetEmailChange(
                                                emailValue
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(horizontalScrollState),
                                    singleLine = true,
                                    placeholder = {
                                        Text(text = stringResource(R.string.reset_password_email))
                                    }
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(80.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            }

                        }
                    },
                    confirmText = stringResource(R.string.reset_password),
                    confirmButton = {
                        onActionAuthentication(
                            AuthenticationAction.ResetPassword(
                                emailValue = authenticationUiState.resetEmailValue,
                                resetPage = ResetPage.LogInPage
                            )
                        )
                    },
                    onDismissButton = {
                        onActionAuthentication(AuthenticationAction.ResetDismiss)
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (logInUiState.isLoading) {
                // Shimmer for textPage
                LoadingShimmer()

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = textPage,
                        style = TitleTypography.copy(color = NewWhite),
                    )
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        EmailAndPassword(
                            Modifier.fillMaxWidth(),
                            emailValue = logInUiState.emailValue,
                            onEmailChange = { emailValue ->
                                onActionLogIn(LogInAction.SetEmail(emailValue))
                            },
                            showPassword = logInUiState.showPassword,
                            passwordValue = logInUiState.passwordValue,
                            onPasswordChange = { passwordValue ->
                                onActionLogIn(LogInAction.OnPasswordChange(passwordValue))
                            }
                        ) {
                            TrailingIcon(
                                imageVector = if (logInUiState.showPassword) Icons.Filled.Visibility else {
                                    Icons.Filled.VisibilityOff
                                },
                                onIconClick = { onActionLogIn(LogInAction.SetShowPassword) }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            AccountTextButton(
                                modifier = Modifier,
                                text = stringResource(R.string.reset_password),
                                textButtonEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                                onSignUpClick = {
                                    onActionAuthentication(AuthenticationAction.OnResetPassword)
                                }
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                        AccountButton(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            containerColor = NewBlue,
                            text = stringResource(R.string.signIn),
                            buttonEnabled = logInUiState.authState == AuthState.NotLoggedIn
                        ) {
                            keyboardController?.hide()
                            onActionLogIn(
                                LogInAction.OnLogIn(
                                    logInUiState.emailValue,
                                    logInUiState.passwordValue
                                )
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        AccountTextButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.dont_have_account) + stringResource(R.string.signup),
                            textButtonEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                            onSignUpClick = onSignUpClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 80.dp)
        )
        Spacer(Modifier.height(40.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Spacer(Modifier.height(24.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Spacer(Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            ShimmerEffect(
                modifier = Modifier
                    .width(100.dp)
                    .height(26.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .padding(horizontal = 60.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .height(30.dp)
                    .width(150.dp)
            )
        }
    }
}

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.presentation.theme.NewBlue
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
    emailValue: String,
    onEmailChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    logInEnabled: Boolean,
    signUpEnabled: Boolean,
    isLoading: Boolean,
    onLogInClick: (email: String, password: String) -> Unit,
    onSignUpClick: () -> Unit,
    onResetEmailValue: String,
    onResetEmailChange: (String) -> Unit,
    resetShowDialog: Boolean,
    resetPassword: () -> Unit,
    resetDismiss: () -> Unit,
    resetIsLoading: Boolean,
    resetPasswordEnabled: Boolean,
    onResetPassword: () -> Unit,
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    SharedScreen(
        modifier = modifier
    ) {
        if (resetShowDialog) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                val horizontalScrollState = rememberScrollState()
                ShowDialog(
                    modifier = Modifier,
                    showDialog = true,
                    title = stringResource(R.string.reset_password),
                    isProgressBar = resetIsLoading,
                    description = {
                        if (!resetIsLoading) {
                            Column {
                                Text(
                                    text = stringResource(R.string.are_you____reset),
                                    style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                                )
                                Spacer(Modifier.height(16.dp))
                                TextField(
                                    value = onResetEmailValue,
                                    onValueChange = {
                                        onResetEmailChange(it)
                                    },
                                    modifier = Modifier.fillMaxWidth().horizontalScroll(horizontalScrollState),
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
                    confirmButton = resetPassword,
                    onDismissButton = resetDismiss
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
            if (isLoading) {

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
                        style = TitleTypography,
                    )
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        EmailAndPassword(
                            Modifier.fillMaxWidth(),
                            emailValue = emailValue,
                            onEmailChange = onEmailChange,
                            showPassword = showPassword,
                            passwordValue = passwordValue,
                            onPasswordChange = onPasswordChange
                        ) {
                            TrailingIcon(imageVector, onIconClick = onIconClick)
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
                                textButtonEnabled = resetPasswordEnabled,
                                onSignUpClick = onResetPassword
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                        AccountButton(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            containerColor = NewBlue,
                            text = stringResource(R.string.signIn),
                            buttonEnabled = logInEnabled
                        ) {
                            keyboardController?.hide()
                            onLogInClick(emailValue, passwordValue)
                        }
                        Spacer(Modifier.height(24.dp))
                        AccountTextButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.dont_have_account) + stringResource(R.string.signup),
                            textButtonEnabled = signUpEnabled,
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

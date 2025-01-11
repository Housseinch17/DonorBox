package com.example.donorbox.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.AccountButton
import com.example.donorbox.presentation.util.AccountTextButton
import com.example.donorbox.presentation.util.EmailAndPassword
import com.example.donorbox.presentation.util.PasswordTextField
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.TextFieldInput
import com.example.donorbox.presentation.util.TrailingIcon
import com.example.donorbox.presentation.util.getPasswordVisualTransformation

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    textPage: String,
    signUpUiState: SignUpUiState,
    signUpAction: (SignUpAction) -> Unit,
    onExistingAccount: () -> Unit
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    SharedScreen {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = textPage,
                style = TitleTypography,
            )
            Spacer(Modifier.height(16.dp))
            TextFieldInput(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(R.string.name),
                value = signUpUiState.name,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Unspecified,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { newNameValue ->
                    signUpAction(SignUpAction.SetName(newNameValue))
                },
                imageVector = Icons.Filled.PersonOutline
            )
            Spacer(Modifier.height(16.dp))
            EmailAndPassword(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                emailValue = signUpUiState.email,
                onEmailChange = { newEmailValue ->
                    signUpAction(SignUpAction.SetEmail(newEmailValue))
                },
                showPassword = signUpUiState.showPassword,
                passwordValue = signUpUiState.password,
                onPasswordChange = { passwordValue ->
                    signUpAction(SignUpAction.SetPassword(passwordValue))
                }
            ) {
                TrailingIcon(imageVector = if (signUpUiState.showPassword) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }, onIconClick = {
                    signUpAction(SignUpAction.SetShowPassword)
                })
            }
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(R.string.confirm_password),
                value = signUpUiState.confirmPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = getPasswordVisualTransformation(signUpUiState.showConfirmPassword),
                onValueChange = { confirmPasswordValue ->
                    signUpAction(SignUpAction.SetConfirmPassword(confirmPasswordValue))
                },
                trailingIcon = {
                    TrailingIcon(
                        imageVector = if (signUpUiState.showConfirmPassword) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        onIconClick = {
                            signUpAction(SignUpAction.SetShowConfirmPassword)
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AccountButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                containerColor = NewBlue,
                text = stringResource(R.string.create_account),
                buttonEnabled = signUpUiState.accountStatus == AccountStatus.NotCreated
            ) {
                keyboardController?.hide()
                signUpAction(
                    SignUpAction.SignUp(
                        name = signUpUiState.name,
                        email = signUpUiState.email,
                        password = signUpUiState.password,
                        confirmPassword = signUpUiState.confirmPassword
                    )
                )
            }
            Spacer(Modifier.height(24.dp))
            AccountTextButton(
                Modifier,
                text = stringResource(R.string.already_have_account),
                textButtonEnabled = signUpUiState.alreadyHaveAccountButton && (signUpUiState.accountStatus == AccountStatus.NotCreated),
                onSignUpClick = onExistingAccount
            )
        }
    }
}
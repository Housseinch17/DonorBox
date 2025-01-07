package com.example.donorbox.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
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
    emailValue: String,
    nameValue: String,
    confirmPasswordValue: String,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    confirmPasswordImageVector: ImageVector,
    onConfirmIconClick: () -> Unit,
    showPassword: Boolean,
    confirmShowPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    buttonText: String,
    accountTextButton: String,
    createAccountEnabled: Boolean,
    alreadyExistingEnabled: Boolean,
    onCreateAccount: (name: String, confirmPassword: String, email: String, password: String) -> Unit,
    onExistingAccount: () -> Unit
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    SharedScreen(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                value = nameValue,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Unspecified,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { newName ->
                    onNameChange(newName)
                },
                imageVector = Icons.Filled.PersonOutline
            )
            Spacer(Modifier.height(16.dp))
            EmailAndPassword(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                emailValue = emailValue,
                onEmailChange = onEmailChange,
                showPassword = showPassword,
                passwordValue = passwordValue,
                onPasswordChange = onPasswordChange
            ) {
                TrailingIcon(imageVector, onIconClick = onIconClick)
            }
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(R.string.confirm_password),
                value = confirmPasswordValue,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = getPasswordVisualTransformation(confirmShowPassword),
                onValueChange = { confirmPassword ->
                    onConfirmPasswordChange(confirmPassword)
                },
                trailingIcon = {
                    TrailingIcon(
                        confirmPasswordImageVector,
                        onIconClick = onConfirmIconClick,
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AccountButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                containerColor = NewBlue,
                text = buttonText,
                buttonEnabled = createAccountEnabled
            ) {
                keyboardController?.hide()
                onCreateAccount(
                    nameValue,
                    confirmPasswordValue,
                    emailValue,
                    passwordValue
                )
            }
            Spacer(Modifier.height(24.dp))
            AccountTextButton(
                Modifier,
                text = accountTextButton,
                textButtonEnabled = alreadyExistingEnabled,
                onSignUpClick = onExistingAccount
            )
        }
    }
}
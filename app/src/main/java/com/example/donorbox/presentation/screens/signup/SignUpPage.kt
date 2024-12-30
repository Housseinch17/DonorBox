package com.example.donorbox.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.donorbox.presentation.util.AccountButton
import com.example.donorbox.presentation.util.AccountTextButton
import com.example.donorbox.presentation.util.EmailAndPassword
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.TrailingIcon

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    textPage: String,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    buttonText: String,
    accountTextButton: String,
    createAccountEnabled: Boolean,
    alreadyExistingEnabled: Boolean,
    onCreateAccount: (email: String, password: String) -> Unit,
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
                textPage, style = MaterialTheme.typography.titleLarge, color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
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
            AccountButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                containerColor = Color.Green,
                text = buttonText,
                buttonEnabled = createAccountEnabled
            ) {
                keyboardController?.hide()
                onCreateAccount(emailValue, passwordValue)
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
package com.example.donorbox.presentation.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.presentation.util.AccountButton
import com.example.donorbox.presentation.util.AccountTextButton
import com.example.donorbox.presentation.util.EmailAndPassword
import com.example.donorbox.presentation.util.TrailingIcon

@Composable
fun SignUpScreen(
    modifier: Modifier,
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
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.donate),
            contentDescription = stringResource(R.string.background_image),
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.matchParentSize().padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(textPage, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Spacer(Modifier.height(16.dp))
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
            Spacer(Modifier.height(16.dp))
            AccountButton(Modifier, buttonText, buttonEnabled = createAccountEnabled) {
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

package com.example.donorbox.presentation.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.presentation.theme.BodyTypography
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.SharedScreen

@Composable
fun ProfilePage(
    modifier: Modifier,
    profileUiState: ProfileUiState,
    contactUs: () -> Unit,
) {
    SharedScreen{
        Box(
            modifier = modifier.padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (profileUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    color = NewGray
                )
            } else {
                Box(modifier = modifier, contentAlignment = Alignment.TopEnd){
                    Button(
                        modifier = Modifier,
                        onClick = contactUs,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NewBlue,
                            contentColor = NewWhite
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.contact_us),
                            style = TitleTypography.copy(color = NewWhite)
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.welcome) + " ${profileUiState.username}!",
                        style = TitleTypography,
                        textAlign = TextAlign.Center
                        )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.name) + ": ${profileUiState.name} ",
                        style = BodyTypography,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
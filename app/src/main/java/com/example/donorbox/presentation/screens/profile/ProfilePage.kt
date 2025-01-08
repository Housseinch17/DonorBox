package com.example.donorbox.presentation.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.SharedScreen

@Composable
fun ProfilePage(
    profileUiState: ProfileUiState
) {
    SharedScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (profileUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    color = NewGray
                )
            } else {
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
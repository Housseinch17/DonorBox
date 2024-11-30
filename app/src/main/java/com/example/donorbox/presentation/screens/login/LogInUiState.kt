package com.example.donorbox.presentation.screens.login

import androidx.compose.runtime.Immutable
import com.example.donorbox.presentation.sealedInterfaces.AuthState

@Immutable
data class LogInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val showPassword: Boolean = false,
    val authState: AuthState = AuthState.NotLoggedIn,
    val userName: String? = null,
    val isLoading: Boolean = true,
    )

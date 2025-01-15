package com.example.donorbox.presentation.screens.login

import androidx.compose.runtime.Immutable

@Immutable
data class LogInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val showPassword: Boolean = false,
    val authState: AuthState = AuthState.NotLoggedIn,
    val userName: String? = null,
    val isLoading: Boolean = true,
    )

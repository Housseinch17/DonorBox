package com.example.donorbox.presentation.screens.signup

import androidx.compose.runtime.Immutable
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus

@Immutable
data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val accountStatus: AccountStatus = AccountStatus.NotCreated,
    val alreadyHaveAccountButton: Boolean = false,
)

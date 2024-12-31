package com.example.donorbox.presentation.screens.settings

import androidx.compose.runtime.Immutable
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement

@Immutable
data class SettingsUiState(
    val currentPasswordValue: String = "",
    val newPasswordValue: String = "",
    val confirmNewPasswordValue: String = "",
    val currentShowPassword: Boolean = false,
    val newShowPassword: Boolean = false,
    val confirmShowPassword: Boolean = false,
    val showText: Boolean = false,
    val confirmShowText: Boolean = false,
    val passwordChangement: PasswordChangement = PasswordChangement.InitialState,
    val isLoading: Boolean = false
)

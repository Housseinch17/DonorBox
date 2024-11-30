package com.example.donorbox.presentation

import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement

data class AuthenticationUiState(
    val signOut: SignOutResponse = SignOutResponse.InitialState,
    val username: String? = "",
    val signOutShowDialog: Boolean = false,
    val resetShowDialog: Boolean = false,
    val resetPassword: PasswordChangement = PasswordChangement.InitialState,
    val resetEmailValue: String = "",
    )

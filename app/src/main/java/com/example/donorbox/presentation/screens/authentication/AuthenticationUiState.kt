package com.example.donorbox.presentation.screens.authentication

import com.example.donorbox.presentation.navigation.NavigationScreens

data class AuthenticationUiState(
    val signOut: SignOutResponse = SignOutResponse.InitialState,
    val username: String? = "",
    val signOutShowDialog: Boolean = false,
    val resetShowDialog: Boolean = false,
    val resetPassword: PasswordChangement = PasswordChangement.InitialState,
    val resetEmailValue: String = "",
    val currentScreen: NavigationScreens = NavigationScreens.HomePage,
    val bottomBarLoading: Boolean = true,
    )

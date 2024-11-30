package com.example.donorbox.presentation.sealedInterfaces

sealed interface AuthState {
    data object LoggedIn : AuthState
    data object NotLoggedIn : AuthState
    data object Loading : AuthState
    data class Error(val message: String) : AuthState
}
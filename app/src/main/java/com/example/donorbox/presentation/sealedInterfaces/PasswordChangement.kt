package com.example.donorbox.presentation.sealedInterfaces

sealed interface PasswordChangement {
    data class Success(val successMessage: String) : PasswordChangement
    data class Error(val errorMessage: String) : PasswordChangement
    data object InitialState : PasswordChangement
    data object IsLoading : PasswordChangement
}
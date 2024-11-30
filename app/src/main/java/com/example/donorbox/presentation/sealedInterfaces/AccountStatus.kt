package com.example.donorbox.presentation.sealedInterfaces

sealed interface AccountStatus {
    data class IsCreated(val message: String) : AccountStatus
    data object NotCreated : AccountStatus
    data object IsLoading : AccountStatus
    data class Error(val error: String) : AccountStatus
}
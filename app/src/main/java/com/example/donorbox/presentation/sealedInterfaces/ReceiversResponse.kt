package com.example.donorbox.presentation.sealedInterfaces

import com.example.donorbox.data.model.Receiver

sealed interface ReceiversResponse {
    data class Success(val receivers:  List<Receiver>): ReceiversResponse
    data object IsLoading: ReceiversResponse
    data class Error(val message: String): ReceiversResponse
}
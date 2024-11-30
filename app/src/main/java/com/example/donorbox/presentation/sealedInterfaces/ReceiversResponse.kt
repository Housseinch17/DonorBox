package com.example.donorbox.presentation.sealedInterfaces

import com.example.donorbox.data.model.Receivers

sealed interface ReceiversResponse {
    data class Success(val receivers: List<Receivers>): ReceiversResponse

    data class Error(val message: String): ReceiversResponse
}
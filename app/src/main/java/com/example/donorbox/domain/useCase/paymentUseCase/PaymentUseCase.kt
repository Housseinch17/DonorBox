package com.example.donorbox.domain.useCase.paymentUseCase

import com.example.donorbox.data.model.payment.PaymentModel

interface PaymentUseCase {
    suspend fun getPayment(amount: Int): PaymentModel
}
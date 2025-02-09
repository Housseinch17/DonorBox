package com.example.donorbox.domain.repository

import com.example.donorbox.data.model.payment.PaymentModel

interface PaymentRepository {
    suspend fun getPayment(amount: Int): PaymentModel
}
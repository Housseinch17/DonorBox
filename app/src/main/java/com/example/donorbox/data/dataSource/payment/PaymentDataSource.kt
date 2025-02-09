package com.example.donorbox.data.dataSource.payment

import com.example.donorbox.data.model.payment.PaymentModel


interface PaymentDataSource {
    suspend fun getPayment(amount: Int): PaymentModel
}
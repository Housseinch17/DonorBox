package com.example.donorbox.data.dataSource.payment

import com.example.donorbox.data.model.payment.PaymentModel
import com.example.donorbox.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val paymentDataSource: PaymentDataSource
) : PaymentRepository {
    override suspend fun getPayment(amount: Int): PaymentModel {
        return paymentDataSource.getPayment(amount)
    }


}
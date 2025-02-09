package com.example.donorbox.domain.useCase.paymentUseCase

import com.example.donorbox.data.model.payment.PaymentModel
import com.example.donorbox.domain.repository.PaymentRepository

class PaymentUseCaseImpl(
    private val paymentRepository: PaymentRepository
) : PaymentUseCase {
    override suspend fun getPayment(amount: Int): PaymentModel {
        return paymentRepository.getPayment(amount)
    }

}
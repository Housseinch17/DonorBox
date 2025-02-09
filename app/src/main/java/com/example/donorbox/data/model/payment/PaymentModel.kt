package com.example.donorbox.data.model.payment

import kotlinx.serialization.Serializable

@Serializable
data class PaymentModel(
    val customerId: String = "",
    val ephemeralId: String = "",
    val ephemeralSecret: String = "",
    val paymentIntentId: String = "",
    val paymentIntentClientSecret: String = "",
)

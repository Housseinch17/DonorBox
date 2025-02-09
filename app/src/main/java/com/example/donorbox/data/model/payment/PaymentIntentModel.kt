package com.example.donorbox.data.model.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentModel(
    val id: String = "",
    @SerialName("client_secret")
    val clientSecret: String = "",

)

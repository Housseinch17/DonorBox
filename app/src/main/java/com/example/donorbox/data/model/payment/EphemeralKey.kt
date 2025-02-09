package com.example.donorbox.data.model.payment

import kotlinx.serialization.Serializable

@Serializable
data class EphemeralKey(
    val id: String = "",
    val secret: String = ""
)

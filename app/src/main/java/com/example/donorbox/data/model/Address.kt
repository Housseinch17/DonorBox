package com.example.donorbox.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

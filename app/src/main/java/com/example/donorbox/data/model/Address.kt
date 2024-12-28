package com.example.donorbox.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Address(
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

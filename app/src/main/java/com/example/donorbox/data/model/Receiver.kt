package com.example.donorbox.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Receiver(
    val phoneNumber: String = "",
    val token: String = "",
    val address: Address = Address(),
    val bank: String = "",
    val name: String = "",
    val omt: String = "",
    val whish: String = "",
    val picUrl: String = ""
)
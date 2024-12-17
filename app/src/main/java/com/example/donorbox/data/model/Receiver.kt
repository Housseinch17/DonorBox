package com.example.donorbox.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Receiver(
    val address: String = "",
    val bank: String = "",
    val name: String = "",
    val omt: String = "",
    val whish: String = "",
    val username: String = "",
    val picUrl: String = ""
)
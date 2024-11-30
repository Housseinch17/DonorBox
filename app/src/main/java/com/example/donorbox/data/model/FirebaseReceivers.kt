package com.example.donorbox.data.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class FirebaseReceivers(
    val receivers: List<Receivers>
)
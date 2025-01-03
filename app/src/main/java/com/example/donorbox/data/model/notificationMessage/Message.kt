package com.example.donorbox.data.model.notificationMessage

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Message(
    val notification: Notification = Notification(),
    val token: String = ""
)
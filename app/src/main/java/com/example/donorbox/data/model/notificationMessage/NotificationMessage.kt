package com.example.donorbox.data.model.notificationMessage

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class NotificationMessage(
    val message: Message = Message()
)
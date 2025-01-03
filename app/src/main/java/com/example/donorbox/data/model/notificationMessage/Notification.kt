package com.example.donorbox.data.model.notificationMessage

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Notification(
    val title: String = "",
    val body: String = ""
)
package com.example.donorbox.data.api

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.presentation.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMApi {
    @POST("v1/projects/donorbox-dc461/messages:send")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
   suspend fun sendNotification(
        @Body notificationMessage: NotificationMessage,
        @Header("Authorization") accessToken: String = "Bearer ${Constants.getAccessToken()}"
    ): Response<NotificationMessage>
}

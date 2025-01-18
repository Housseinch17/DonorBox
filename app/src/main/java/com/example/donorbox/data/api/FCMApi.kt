package com.example.donorbox.data.api

import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import retrofit2.Response
import retrofit2.http.Body
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
    ): Response<NotificationMessage>
}

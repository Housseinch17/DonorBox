package com.example.donorbox.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.donorbox.R
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.UpdateDeviceTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            saveTokenToFirebase(token = token)
            Log.d("FCM", "onNewToken $token")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyTag", "onMessageReceived() received message")

        // Handle data payload or from notification data
        remoteMessage.data.let { data ->
            val title = data["title"] ?: remoteMessage.notification?.title
            val message = data["message"] ?: remoteMessage.notification?.body
            Log.d("FCM","title: $title and message: $message")
            showNotification(title, message)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "my_notification_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(title ?: "Notification")
            .setContentText(message)
            .setSmallIcon(R.drawable.omt) // Your custom icon
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }



    private suspend fun saveTokenToFirebase(token: String){
        updateDeviceTokenUseCase.updateDeviceToken(token)
        Log.d("FCM", "saveTokenToFirebase: $token")
    }
}
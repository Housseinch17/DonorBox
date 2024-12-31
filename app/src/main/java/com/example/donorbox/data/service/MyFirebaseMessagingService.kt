package com.example.donorbox.data.service

import android.util.Log
import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
): FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            saveTokenToFirebase(token = token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            Log.d("FCM", "Message Notification: ${it.body}")
            // Show notification to the user
        }
    }

    private suspend fun saveTokenToFirebase(username: String = "alMazraa*gmail_com", token: String){
        firebaseRepositoryImpl.writeToken(username,token)
    }
}
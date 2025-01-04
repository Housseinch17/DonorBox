package com.example.donorbox.data.dataSource.firebase.firebaseNotification

import android.util.Log
import com.example.donorbox.data.api.FCMApi
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSourceImpl
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.presentation.util.Constants.replaceUsername
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseNotificationDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val firebaseAuthenticationDataSourceImpl: FirebaseAuthenticationDataSourceImpl,
    private val firebaseReadDataSourceImpl: FirebaseReadDataSourceImpl,
    private val firebaseWriteDataSourceImpl: FirebaseWriteDataSourceImpl,
    private val fcmApi: FCMApi
): FirebaseNotificationDataSource {

    override suspend fun fetchToken(): String = withContext(coroutineDispatcher){
        suspendCoroutine { continuation ->
            firebaseMessaging.token.addOnCompleteListener { task->
                if(task.isSuccessful){
                    val token = task.result
                    continuation.resume(token)
                }else{
                    continuation.resume("")
                }
            }
        }
    }

    override suspend fun updateDeviceToken(token: String) {
        var username = getCurrentUsername()
        username = replaceUsername(username)
        if (getAllReceivers().contains(username)) {
            val newToken = token.ifEmpty { fetchToken() }
            try {
                firebaseWriteDataSourceImpl.writeToken(username = username!!, token = newToken)
                Log.d("MyTag","updateTokenIntoFirebase() successfully completed and token: $newToken")
            }catch (e: Exception){
                Log.e("MyTag","updateTokenIntoFirebase() ${e.message}")
            }
        }
    }

    override suspend fun sendNotificationToToken(notificationMessage: NotificationMessage): Unit = withContext(coroutineDispatcher){
          try {
              val response = fcmApi.sendNotification(notificationMessage)
              if (response.isSuccessful) {
                  Log.d("FCM", "Notification sent successfully")
              } else {
                  val responseBody = response.errorBody()?.string()

                  Log.e("FCM", "Failed to send notification: $responseBody")
              }
          } catch (e: Exception) {
              Log.e("FCM", "Exception while sending notification", e)
          }
    }

    private suspend fun getAllReceivers(): List<String> {
        return firebaseReadDataSourceImpl.getAllReceivers()
    }

    private suspend fun getCurrentUsername(): String?{
        return firebaseAuthenticationDataSourceImpl.getCurrentUser()
    }

}
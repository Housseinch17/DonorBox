package com.example.donorbox.data.dataSource.firebase.firebaseNotification

import android.content.Context
import android.util.Log
import com.example.donorbox.data.api.FCMApi
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.presentation.util.Constants.replaceUsername
import com.example.donorbox.presentation.util.isInternetAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseNotificationDataSourceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val auth: FirebaseAuth,
    private val receiversDatabaseReference: DatabaseReference,
    private val context: Context,
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
                writeToken(username = username!!, token = newToken)
                Log.d("MyTag","updateTokenIntoFirebase() successfully completed and token: $newToken")
            }catch (e: Exception){
                Log.e("MyTag","updateTokenIntoFirebase() ${e.message}")
            }
        }
    }

    override suspend fun sendNotificationToToken(notificationMessage: NotificationMessage): Unit = withContext(coroutineDispatcher){
          try {
              Log.d("MyTag","sendNotification notificaimpl")
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

    private suspend fun getAllReceivers(): List<String> = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            val hasInternet = context.isInternetAvailable()
            if (hasInternet) {
                try {
                    receiversDatabaseReference.get().addOnSuccessListener { snapshot ->
                        val receiversList = snapshot.children.mapNotNull {
                            it.key
                        }
                        continuation.resume(receiversList)
                    }
                } catch (e: Exception) {
                    continuation.resume(emptyList())
                }
            } else {
                continuation.resume((emptyList()))
            }
        }
    }

    private suspend fun getCurrentUsername(): String?= withContext(coroutineDispatcher){
        return@withContext try {
            auth.currentUser?.email
        } catch (e: Exception) {
            Log.e("MyTag", "getCurrentUser() ${e.message}")
            null
        }
    }

    private suspend fun writeToken(username: String, token: String): Unit = withContext(coroutineDispatcher){
        val receiverRef = receiversDatabaseReference.child(username)
        receiverRef.child("token").setValue(token).addOnSuccessListener {
            // Handle success, for example:
            Log.d("Firebase", "writeToken() Token updated successfully for $username")
        }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("Firebase", "writeToken() Failed to update token: ${exception.message}")
            }
    }
}
package com.example.donorbox.data.dataSource.firebase.firebaseNotification

import android.util.Log
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSourceImpl
import com.example.donorbox.data.model.NotificationData
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
    private val firebaseWriteDataSourceImpl: FirebaseWriteDataSourceImpl
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

    override suspend fun updateDeviceToken() {
        var username = getCurrentUsername()
        username = username?.replace("@", "*")?.replace(".", "_")
        if (getAllReceivers().contains(username)) {
            val token = fetchToken()
            try {
                firebaseWriteDataSourceImpl.writeToken(username = username!!, token = token)
                Log.d("MyTag","updateTokenIntoFirebase() successfully completed and token: $token")
            }catch (e: Exception){
                Log.e("MyTag","updateTokenIntoFirebase() ${e.message}")
            }
        }
    }

    override suspend fun handleNotification(notificationData: NotificationData) {

    }

    private suspend fun getAllReceivers(): List<String> {
        return firebaseReadDataSourceImpl.getAllReceivers()
    }

    private suspend fun getCurrentUsername(): String?{
        return firebaseAuthenticationDataSourceImpl.getCurrentUser()
    }

}
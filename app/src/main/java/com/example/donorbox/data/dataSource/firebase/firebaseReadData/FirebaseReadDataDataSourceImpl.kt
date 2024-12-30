package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse
import com.example.donorbox.presentation.util.isInternetAvailable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseReadDataDataSourceImpl(
    private val databaseReference: DatabaseReference,
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher,
) : FirebaseReadDataDataSource {
    override suspend fun readReceivers(): ReceiversResponse = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            val hasInternet = context.isInternetAvailable()
            if (hasInternet) {
                try {
                    //addValueEventListener will crash everytime we update the firebase
                    //addListenerForSingleValueEvent will avoid crashing when we update firebase
                    //its like reading only once and not keep on reading
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val receivers =
                                snapshot.children.mapNotNull { it.getValue(Receiver::class.java) }
                            if (receivers.isNotEmpty()) {
                                continuation.resume(
                                    ReceiversResponse.Success(
                                        receivers = receivers
                                    )
                                )
                            } else {
                                continuation.resume(ReceiversResponse.Error("Data is null"))
                            }
                        }

                        @SuppressLint("SuspiciousIndentation")
                        override fun onCancelled(error: DatabaseError) {
                            val errorMessage = "Failed to read menu data $error"
                            continuation.resume(ReceiversResponse.Error(errorMessage))
                        }
                    })
                } catch (e: Exception) {
                    val errorMessage = e.message ?: "Something went wrong"
                    continuation.resume(ReceiversResponse.Error(errorMessage))
                }
            } else {
                continuation.resume(ReceiversResponse.Error("No internet connection!"))
            }
        }
    }

    override suspend fun getAllReceivers(): List<String> =
        suspendCoroutine { continuation ->
            val hasInternet = context.isInternetAvailable()
            if (hasInternet) {
                try {
                    databaseReference.get().addOnSuccessListener { snapshot ->
                        val receiversList = snapshot.children.mapNotNull {
                            it.key
                        }
                        continuation.resume(receiversList)
                        Log.d("MyTag", "$receiversList")
                    }
                } catch (e: Exception) {
                    continuation.resume(emptyList())
                }
            } else {
                continuation.resume((emptyList()))
            }
        }
}
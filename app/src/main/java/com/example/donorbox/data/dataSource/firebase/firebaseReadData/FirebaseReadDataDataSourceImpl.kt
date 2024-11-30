package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import android.content.Context
import com.example.donorbox.data.model.FirebaseReceivers
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
                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val receivers =
                                snapshot.getValue(FirebaseReceivers::class.java)?.receivers
                            if (receivers != null) {
                                continuation.resume(
                                    ReceiversResponse.Success(
                                        receivers = receivers
                                    )
                                )
                            } else {
                                continuation.resume(ReceiversResponse.Error("Data is null"))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            val errorMessage = "Failed to read menu data$error"
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
}
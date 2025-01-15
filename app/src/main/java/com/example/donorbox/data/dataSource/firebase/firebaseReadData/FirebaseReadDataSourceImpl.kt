package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import android.annotation.SuppressLint
import android.content.Context
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.presentation.screens.home.ReceiversResponse
import com.example.donorbox.presentation.util.Constants.replaceUsername
import com.example.donorbox.presentation.util.isInternetAvailable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseReadDataSourceImpl(
    private val auth: FirebaseAuth,
    private val receiversDatabaseReference: DatabaseReference,
    private val usersDatabaseReference: DatabaseReference,
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher,
) : FirebaseReadDataSource {
    override suspend fun readReceivers(): ReceiversResponse = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            val hasInternet = context.isInternetAvailable()
            if (hasInternet) {
                try {
                    //addValueEventListener will crash everytime we update the firebase
                    //addListenerForSingleValueEvent will avoid crashing when we update firebase
                    //its like reading only once and not keep on reading
                    receiversDatabaseReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val receivers = snapshot.children.mapNotNull {
                                it.getValue(Receiver::class.java)
                            }
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

    override suspend fun readFullNameByUsername(): String =
        withContext(coroutineDispatcher) {
            val user = replaceUsername(auth.currentUser?.email) ?: ""
            suspendCoroutine { continuation ->
                val userRef = usersDatabaseReference.child(user)
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val name = snapshot.child("name").getValue(String::class.java)
                            continuation.resume(
                                "$name"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resume("")
                    }
                })
            }
        }

    override suspend fun readAllDonations(): List<String> = withContext(coroutineDispatcher) {
        var newUsername = auth.currentUser?.email ?: ""
        newUsername = replaceUsername(newUsername)!!
        val receiversList = getAllReceivers()
        val usernameIsReceiver = receiversList.contains(newUsername)
        suspendCoroutine { continuation ->
            if (newUsername.isNotEmpty() && usernameIsReceiver) {
                val donationsRef =
                    receiversDatabaseReference.child(newUsername).child("donationsReceived")
                donationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val donationsMap = snapshot.getValue(object :
                            GenericTypeIndicator<Map<String, String>>() {})
                        // Extract values and convert to list
                        val donationsList = donationsMap?.values?.toList() ?: emptyList()
                        continuation.resume(donationsList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resume(emptyList())
                    }
                })
            } else {
                continuation.resume(emptyList())
            }
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
}
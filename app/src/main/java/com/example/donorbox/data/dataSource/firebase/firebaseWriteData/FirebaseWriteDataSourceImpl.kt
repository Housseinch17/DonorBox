package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

import android.util.Log
import com.example.donorbox.presentation.util.Constants.replaceUsername
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FirebaseWriteDataSourceImpl(
    private val receiversDatabaseReference: DatabaseReference,
    private val usersDatabaseReference: DatabaseReference,
    private val coroutineDispatcher: CoroutineDispatcher
) : FirebaseWriteDataSource {
    override suspend fun writeToken(username: String, token: String): Unit =
        withContext(coroutineDispatcher) {
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

    override suspend fun addUsers(username: String, name: String): Unit =
        withContext(coroutineDispatcher) {
            // Create a map for the nested structure
            /*        val userMap = mapOf(
                        "name" to name,
                        "family" to family
                    )*/

            //here we don't need family any more
            val userMap = mapOf(
                "name" to name,
            )
            val user = replaceUsername(username)
            // Set the value under the username node
            usersDatabaseReference.child(user!!).setValue(userMap).addOnSuccessListener {
                Log.d("Firebase", "addUsers() successfully completed")
            }.addOnFailureListener { exception ->
                Log.e("Firebase", " addUsers() error: ${exception.message}")
            }


        }

    override suspend fun writeDonationsIntoFirebase(username: String, donation: String): Unit =
        withContext(coroutineDispatcher) {
            val newUsername = replaceUsername(username)
            Log.d("MyTag", "newUsername: $newUsername")
            if (newUsername!!.isNotEmpty()) {
                val receiverRef =
                    receiversDatabaseReference.child(newUsername).child("donationsReceived")
                // Add the donation string as a new child in the donationsReceived list
                receiverRef.push().setValue(donation)
                    .addOnSuccessListener {
                        Log.d(
                            "Firebase",
                            "writeDonationsIntoFirebase(): Donation added successfully."
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "Firebase",
                            "writeDonationsIntoFirebase(): Failed to donate ${exception.message}"
                        )
                    }
            }
        }
}
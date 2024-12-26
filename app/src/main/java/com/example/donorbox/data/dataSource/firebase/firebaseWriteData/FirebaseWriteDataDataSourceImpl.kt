package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

import android.util.Log
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FirebaseWriteDataDataSourceImpl (
    private val databaseReference: DatabaseReference,
    private val coroutineDispatcher: CoroutineDispatcher,
) : FirebaseWriteDataDataSource {
    override suspend fun writeToken(username: String, token: String): Unit = withContext(coroutineDispatcher){
        val userRef = databaseReference.child(username)
        userRef.child("token").setValue(token).addOnSuccessListener {
            // Handle success, for example:
            Log.d("Firebase", "Token updated successfully for $username")
        }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("Firebase", "Failed to update token: ${exception.message}")
            }
    }
}
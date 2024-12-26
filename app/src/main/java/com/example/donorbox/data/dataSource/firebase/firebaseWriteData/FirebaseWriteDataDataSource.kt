package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

interface FirebaseWriteDataDataSource{
    suspend fun writeToken(username: String, token: String)
}
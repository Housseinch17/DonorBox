package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

interface FirebaseWriteDataDataSource{
    suspend fun writeToken(username: String, token: String)
    suspend fun addUsers(username: String, name: String, family: String)
}
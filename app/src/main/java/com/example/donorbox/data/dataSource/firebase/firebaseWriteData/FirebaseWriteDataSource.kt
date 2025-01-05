package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

interface FirebaseWriteDataSource{
    suspend fun writeToken(username: String, token: String)
    suspend fun addUsers(username: String, name: String, family: String)
    suspend fun writeDonationsIntoFirebase(username: String, donation: String)

}
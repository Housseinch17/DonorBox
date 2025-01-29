package com.example.donorbox.data.dataSource.firebase.firebaseWriteData

import com.example.donorbox.data.model.ContactUs

interface FirebaseWriteDataSource{
    suspend fun writeToken(username: String, token: String)
    suspend fun addUsers(username: String, name: String)
    suspend fun writeDonationsIntoFirebase(username: String, donation: String)
    suspend fun contactUs(contactUs: ContactUs, username: String): String

}
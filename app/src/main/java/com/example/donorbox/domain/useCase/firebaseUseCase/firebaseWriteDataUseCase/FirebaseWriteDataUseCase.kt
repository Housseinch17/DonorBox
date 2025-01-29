package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.data.model.ContactUs

interface FirebaseWriteDataUseCase {
    suspend fun addUser(username: String, name: String)
    suspend fun writeDonationsIntoFirebase(username: String, donation: String)
    suspend fun writeToken(username: String, token: String)
    suspend fun contactUs(contactUs: ContactUs, username: String): String
}
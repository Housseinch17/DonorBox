package com.example.donorbox.data.dataSource.firebase.firebaseReadData


import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseReadDataSource{
    suspend fun readReceivers(): ReceiversResponse
    suspend fun readFullNameByUsername(): String
    suspend fun readAllDonations(): List<String>

}
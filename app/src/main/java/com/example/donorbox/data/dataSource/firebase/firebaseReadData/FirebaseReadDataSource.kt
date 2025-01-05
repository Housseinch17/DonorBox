package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import com.example.donorbox.presentation.screens.home.FullName
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseReadDataSource{
    suspend fun readReceivers(): ReceiversResponse
    suspend fun readFullNameByUsername(): FullName
    suspend fun readAllDonations(): List<String>

}
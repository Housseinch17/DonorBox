package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import com.example.donorbox.presentation.screens.home.FullName
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseReadDataDataSource{
    suspend fun readReceivers(): ReceiversResponse
    suspend fun getAllReceivers(): List<String>
    suspend fun readFullNameByUsername(): FullName

}
package com.example.donorbox.data.dataSource.firebase.firebaseReadData

import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

interface FirebaseReadDataDataSource{
    suspend fun readReceivers(): ReceiversResponse
}
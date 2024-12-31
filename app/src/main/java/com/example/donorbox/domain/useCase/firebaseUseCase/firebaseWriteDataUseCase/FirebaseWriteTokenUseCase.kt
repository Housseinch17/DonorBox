package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSourceImpl

class FirebaseWriteTokenUseCase(private val writeDataDataSourceImpl: FirebaseWriteDataSourceImpl) {
    suspend fun writeToken(username: String, token: String) = writeDataDataSourceImpl.writeToken(username,token)
}
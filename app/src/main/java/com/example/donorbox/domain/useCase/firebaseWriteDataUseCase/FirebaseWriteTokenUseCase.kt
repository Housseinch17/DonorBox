package com.example.donorbox.domain.useCase.firebaseWriteDataUseCase

import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl

class FirebaseWriteTokenUseCase(private val writeDataDataSourceImpl: FirebaseWriteDataDataSourceImpl) {
    suspend fun writeToken(username: String, token: String) = writeDataDataSourceImpl.writeToken(username,token)
}
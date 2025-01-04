package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class FirebaseAddUserUseCase(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun addUser(username: String, name: String, family: String) = firebaseRepositoryImpl.addUser(username,name,family)
}
package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseAddUserUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun addUser(username: String, name: String) = firebaseRepository.addUser(username,name)
}
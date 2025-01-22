package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository

class FirebaseWriteDataUseCaseImpl(
    private val firebaseRepository: FirebaseRepository
): FirebaseWriteDataUseCase {
    override suspend fun addUser(username: String, name: String) = firebaseRepository.addUser(username,name)

    override suspend fun writeDonationsIntoFirebase(username: String, donation: String) =
        firebaseRepository.writeDonationsIntoFirebase(username, donation)

    override suspend fun writeToken(username: String, token: String) = firebaseRepository.writeToken(username,token)
}
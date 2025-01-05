package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.home.FullName

class FirebaseReadFullNameUseCase(
private val firebaseRepository: FirebaseRepository
) {
    suspend fun readFullNameByUsername(): FullName =
        firebaseRepository.readFullNameByUsername()
}
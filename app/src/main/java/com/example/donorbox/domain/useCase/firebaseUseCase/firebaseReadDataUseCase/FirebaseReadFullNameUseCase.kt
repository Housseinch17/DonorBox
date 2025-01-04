package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.presentation.screens.home.FullName

class FirebaseReadFullNameUseCase(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun readFullNameByUsername(): FullName =
        firebaseRepositoryImpl.readFullNameByUsername()
}
package com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl

class SignOutUseCase (
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun signOut() = firebaseRepositoryImpl.signOut()
}
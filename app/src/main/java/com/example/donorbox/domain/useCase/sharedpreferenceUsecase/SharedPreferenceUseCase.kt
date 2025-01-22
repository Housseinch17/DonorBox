package com.example.donorbox.domain.useCase.sharedpreferenceUsecase

interface SharedPreferenceUseCase {
    suspend fun getUsername(): String?
    suspend fun saveUsername(username: String?)
}
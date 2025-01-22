package com.example.donorbox.domain.useCase.sharedpreferenceUsecase

import com.example.donorbox.domain.repository.SharedPreferencesRepository

class SharedPreferenceUseCaseImpl(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): SharedPreferenceUseCase {
    override suspend fun getUsername(): String? = sharedPreferencesRepository.getUsername()

    override suspend fun saveUsername(username: String?) = sharedPreferencesRepository.saveUsername(username)
}
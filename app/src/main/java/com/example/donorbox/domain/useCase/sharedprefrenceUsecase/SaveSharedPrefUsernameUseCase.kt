package com.example.donorbox.domain.useCase.sharedprefrenceUsecase

import com.example.donorbox.domain.repository.SharedPreferencesRepository

class SaveSharedPrefUsernameUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend fun saveUsername(username: String?) = sharedPreferencesRepository.saveUsername(username)
}
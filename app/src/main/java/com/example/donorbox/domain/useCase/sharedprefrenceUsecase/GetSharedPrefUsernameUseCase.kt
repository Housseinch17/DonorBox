package com.example.donorbox.domain.useCase.sharedprefrenceUsecase

import com.example.donorbox.domain.repository.SharedPreferencesRepository

class GetSharedPrefUsernameUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend fun getUsername(): String? = sharedPreferencesRepository.getUsername()
}
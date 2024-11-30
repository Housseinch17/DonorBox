package com.example.donorbox.domain.useCase.sharedprefrenceUsecase

import com.example.donorbox.data.dataSource.sharedpreference.SharedPreferencesRepositoryImpl

class GetSharedPrefUsernameUseCase(
    private val sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
) {
    suspend fun getUsername(): String? = sharedPreferencesRepositoryImpl.getUsername()
}
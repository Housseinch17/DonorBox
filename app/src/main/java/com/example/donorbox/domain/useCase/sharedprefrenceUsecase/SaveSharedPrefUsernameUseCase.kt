package com.example.donorbox.domain.useCase.sharedprefrenceUsecase

import com.example.donorbox.data.dataSource.sharedpreference.SharedPreferencesRepositoryImpl

class SaveSharedPrefUsernameUseCase(
    private val sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
) {
    suspend fun saveUsername(username: String?) = sharedPreferencesRepositoryImpl.saveUsername(username)
}
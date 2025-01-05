package com.example.donorbox.data.dataSource.sharedpreference

import com.example.donorbox.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(
    private val sharedPreferencesManager: SharedPreferencesManager
): SharedPreferencesRepository {
    override suspend fun saveUsername(username: String?) {
        sharedPreferencesManager.saveUsername(username)
    }
    override suspend fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }
}
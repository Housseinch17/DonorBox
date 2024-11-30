package com.example.donorbox.data.dataSource.sharedpreference

import com.example.donorbox.domain.repository.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(
    private val sharedPreferencesManagerImpl: SharedPreferencesManagerImpl
): SharedPreferencesRepository {
    override suspend fun saveUsername(username: String?) {
        sharedPreferencesManagerImpl.saveUsername(username)
    }
    override suspend fun getUsername(): String? {
        return sharedPreferencesManagerImpl.getUsername()
    }
}
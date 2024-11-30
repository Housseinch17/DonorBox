package com.example.donorbox.domain.repository

interface SharedPreferencesRepository {
    suspend fun saveUsername(username: String?)
    suspend fun getUsername(): String?
}
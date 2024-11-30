package com.example.donorbox.data.dataSource.sharedpreference

interface SharedPreferencesManager {
    suspend fun saveUsername(username: String?)
    suspend fun getUsername(): String?
}
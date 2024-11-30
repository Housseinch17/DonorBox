package com.example.donorbox.data.dataSource.sharedpreference

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SharedPreferencesManagerImpl(
        private val sharedPreferences: SharedPreferences,
        private val coroutineDispatcher: CoroutineDispatcher
    ): SharedPreferencesManager {

        override suspend fun saveUsername(username: String?) = withContext(coroutineDispatcher){
            sharedPreferences.edit().putString("username", username).apply()
        }

       override suspend fun getUsername(): String? = withContext(coroutineDispatcher){
            return@withContext sharedPreferences.getString("username", null)
        }
    }
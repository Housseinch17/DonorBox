package com.example.donorbox.presentation.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.donorbox.data.dataSource.sharedpreference.SharedPreferencesManager
import com.example.donorbox.data.dataSource.sharedpreference.SharedPreferencesManagerImpl
import com.example.donorbox.data.dataSource.sharedpreference.SharedPreferencesRepositoryImpl
import com.example.donorbox.domain.repository.SharedPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object SharedPreferenceModule {
    val sharedPreferenceModule = module {
        single<SharedPreferences> {
            androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        }

        single<SharedPreferencesManagerImpl> {
            SharedPreferencesManagerImpl(
                sharedPreferences = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<SharedPreferencesRepositoryImpl> {
            SharedPreferencesRepositoryImpl(
                sharedPreferencesManagerImpl = get()
            )
        }

        single<SharedPreferencesManager> {
            SharedPreferencesManagerImpl(
                sharedPreferences = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<SharedPreferencesRepository> {
            SharedPreferencesRepositoryImpl(get())
        }


    }
}
package com.example.donorbox.presentation.di

import android.app.Application
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppModule {
    private val coroutineDispatcherModule = module {
        single<CoroutineDispatcher>(named("Dispatchers.IO")) { Dispatchers.IO }
    }
    private val androidContextModule = module {
        single { androidContext() as Application }
    }

    val appModule =
        listOf(
            coroutineDispatcherModule,
            androidContextModule,
            FirebaseModule.firebaseModule,
            SharedPreferenceModule.sharedPreferenceModule,
            UseCaseModule.useCaseModule,
            ViewModelModule.viewModelModule,
        )
}
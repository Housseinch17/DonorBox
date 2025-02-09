package com.example.donorbox.presentation.di

import android.app.Application
import com.example.donorbox.presentation.di.modules.DataBaseModule
import com.example.donorbox.presentation.di.modules.FirebaseModule
import com.example.donorbox.presentation.di.modules.LocalDataBaseModule
import com.example.donorbox.presentation.di.modules.NetworkModule
import com.example.donorbox.presentation.di.modules.PaymentModule
import com.example.donorbox.presentation.di.modules.Services
import com.example.donorbox.presentation.di.modules.SharedPreferenceModule
import com.example.donorbox.presentation.di.modules.UseCaseModule
import com.example.donorbox.presentation.di.modules.ViewModelModule
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
        single<Application> { androidContext() as Application }
    }

    val appModule =
        listOf(
            coroutineDispatcherModule,
            androidContextModule,
            FirebaseModule.firebaseModule,
            SharedPreferenceModule.sharedPreferenceModule,
            DataBaseModule.databaseModule,
            LocalDataBaseModule.localDataBaseModule,
            PaymentModule.paymentModule,
            UseCaseModule.useCaseModule,
            ViewModelModule.viewModelModule,
            Services.servicesModule,
            NetworkModule.networkModule,

        )
}
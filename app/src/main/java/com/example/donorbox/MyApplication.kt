package com.example.donorbox

import android.app.Application
import com.example.donorbox.presentation.di.AppModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //initialize firebase to read data
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MyApplication)
            modules(AppModule.appModule)
        }
    }
}
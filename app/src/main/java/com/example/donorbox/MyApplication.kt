package com.example.donorbox

import android.app.Application
import com.example.donorbox.presentation.di.AppModule
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    //Job() will cancel/fail the scope when any child cancel/fail
    val applicationScope = CoroutineScope(Job() + Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
        //initialize firebase
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MyApplication)
            modules(AppModule.appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }
}
package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.service.MyFirebaseMessagingService
import org.koin.dsl.module

object Services {
    val servicesModule = module{
        single<MyFirebaseMessagingService>{
            MyFirebaseMessagingService()
        }
    }
}
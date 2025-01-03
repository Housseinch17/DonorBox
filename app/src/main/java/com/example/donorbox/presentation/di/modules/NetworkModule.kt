package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.api.FCMApi
import com.example.donorbox.presentation.util.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkModule {
    val networkModule = module {
        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }


        single<FCMApi> {
            get<Retrofit>().create(FCMApi::class.java)
        }
    }
}
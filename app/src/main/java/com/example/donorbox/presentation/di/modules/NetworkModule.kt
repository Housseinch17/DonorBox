package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.api.FCMApi
import com.example.donorbox.presentation.util.Constants
import com.example.donorbox.presentation.util.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkModule {
    val networkModule = module {

        val authInterceptor = Interceptor { chain ->
            // Intercept the request
            val originalRequest = chain.request()

            // Add the Authorization header with the token
            val requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${Constants.getAccessToken()}")
                .build()

            // Proceed with the modified request
            chain.proceed(requestWithToken)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }


        single<FCMApi> {
            get<Retrofit>().create(FCMApi::class.java)
        }
    }
}
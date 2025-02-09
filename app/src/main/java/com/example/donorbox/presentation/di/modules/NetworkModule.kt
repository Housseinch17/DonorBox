package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.api.FCMApi
import com.example.donorbox.data.api.PaymentApi
import com.example.donorbox.presentation.util.Constants
import com.example.donorbox.presentation.util.Constants.SECRET_KEY
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkModule {
    private const val BASE_URL_FIREBASEMESSAGING = "https://fcm.googleapis.com/"
    private const val BASE_URL_STRIPEPAYMENT = "https://api.stripe.com/v1/"

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

        val firebaseClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        single<Retrofit>(named("FirebaseMessaging")) {
            Retrofit.Builder()
                .baseUrl(BASE_URL_FIREBASEMESSAGING)
                .client(firebaseClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        val paymentInterceptor = Interceptor { chain ->
            // Intercept the request
            val originalRequest = chain.request()

            // Add the Authorization header with the token
            val requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer $SECRET_KEY")
                .build()

            // Proceed with the modified request
            chain.proceed(requestWithToken)
        }

        val paymentClient = OkHttpClient.Builder()
            .addInterceptor(paymentInterceptor)
            .build()

        single<Retrofit>(named("StripePayment")) {
            Retrofit.Builder()
                .baseUrl(BASE_URL_STRIPEPAYMENT)
                .client(paymentClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        single<FCMApi> {
            get<Retrofit>(named("FirebaseMessaging")).create(FCMApi::class.java)
        }

        single<PaymentApi> {
            get<Retrofit>(named("StripePayment")).create(PaymentApi::class.java)
        }
    }

}
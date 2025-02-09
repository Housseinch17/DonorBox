package com.example.donorbox.data.api

import com.example.donorbox.data.model.payment.CustomerModel
import com.example.donorbox.data.model.payment.EphemeralKey
import com.example.donorbox.data.model.payment.PaymentIntentModel
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApi {
    @POST("customers")
    suspend fun getCustomers(): Response<CustomerModel>

    @Headers("Stripe-Version: 2025-01-27.acacia")
    @POST("ephemeral_keys")
    suspend fun getEphemeralKey(
        @Query("customer") customer: String
    ): Response<EphemeralKey>

    @POST("payment_intents")
    suspend fun getPaymentIntent(
        @Query("customer") customer: String,
        @Query("amount") amount: Int,
        @Query("currency") currency: String = "usd",
        @Query("automatic_payment_methods[enabled]") automatePay: Boolean = true
    ): Response<PaymentIntentModel>

}
package com.example.donorbox.data.dataSource.payment

import android.util.Log
import com.example.donorbox.data.api.PaymentApi
import com.example.donorbox.data.model.payment.CustomerModel
import com.example.donorbox.data.model.payment.EphemeralKey
import com.example.donorbox.data.model.payment.PaymentIntentModel
import com.example.donorbox.data.model.payment.PaymentModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PaymentDataSourceImpl(
    private val paymentApi: PaymentApi,
    private val coroutineDispatchers: CoroutineDispatcher
) : PaymentDataSource {

    override suspend fun getPayment(amount: Int): PaymentModel = withContext(coroutineDispatchers) {
        try {
            val customerId = getCustomers()
            Log.d("GetPayment","customerId: $customerId")
            val ephemeralKey = getEphemeralKey(customer = customerId.id)
            Log.d("GetPayment","ephemeralKey: $ephemeralKey")
            val paymentIntent = getPaymentIntent(customer = customerId.id, amount = amount)
            Log.d("GetPayment","paymentIntent: $paymentIntent")
            return@withContext PaymentModel(
                customerId = customerId.id,
                ephemeralId = ephemeralKey.id,
                ephemeralSecret = ephemeralKey.secret,
                paymentIntentId = paymentIntent.id,
                paymentIntentClientSecret = paymentIntent.clientSecret
            )
        }catch (e: Exception){
            return@withContext PaymentModel()
        }

    }

    private suspend fun getCustomers(): CustomerModel = withContext(coroutineDispatchers) {
        try {
            val response = paymentApi.getCustomers()
            Log.d("MyTag", "PaymentDataSourceImpl: getCustomers Succeed, response: ${response.body()}")
            return@withContext CustomerModel(id = response.body()?.id ?: "")
        } catch (e: Exception) {
            Log.e("PaymentDataSourceImpl", "getCustomers(): ${e.message}")
            return@withContext CustomerModel()
        }
    }

    private suspend fun getEphemeralKey(customer: String): EphemeralKey =
        withContext(coroutineDispatchers) {
            try {
                val response = paymentApi.getEphemeralKey(customer)
                Log.d(
                    "MyTag",
                    "PaymentDataSourceImpl: getEphemeralKey Succeed, response: ${response.body()}"
                )
                return@withContext EphemeralKey(
                    id = response.body()?.id ?: "",
                    secret = response.body()?.secret ?: ""
                )
            } catch (e: Exception) {
                Log.e("PaymentDataSourceImpl", "getEphemeralKey(): ${e.message}")
                return@withContext EphemeralKey()
            }
        }

    private suspend fun getPaymentIntent(customer: String, amount: Int): PaymentIntentModel =
        withContext(coroutineDispatchers) {
            try {
                val response = paymentApi.getPaymentIntent(customer, amount)
                Log.d(
                    "MyTag",
                    "PaymentDataSourceImpl: getPaymentIntent Succeed, response: ${response.body()}"
                )
                return@withContext PaymentIntentModel(
                    id = response.body()?.id ?: "",
                    clientSecret = response.body()?.clientSecret ?: ""
                )
            } catch (e: Exception) {
                Log.e("PaymentDataSourceImpl", "getPaymentIntent(): ${e.message}")
                return@withContext PaymentIntentModel()
            }
        }
}
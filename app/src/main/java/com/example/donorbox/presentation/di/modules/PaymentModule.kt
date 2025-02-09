package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.dataSource.payment.PaymentDataSource
import com.example.donorbox.data.dataSource.payment.PaymentDataSourceImpl
import com.example.donorbox.data.dataSource.payment.PaymentRepositoryImpl
import com.example.donorbox.domain.repository.PaymentRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
object PaymentModule {
    val paymentModule = module {

        single<PaymentDataSource>{
            PaymentDataSourceImpl(
                paymentApi = get(),
                coroutineDispatchers = get(named("Dispatchers.IO"))
            )
        }

        single<PaymentRepository>{
            PaymentRepositoryImpl(
                paymentDataSource = get()
            )
        }
    }
}


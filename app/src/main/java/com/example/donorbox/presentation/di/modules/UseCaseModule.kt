package com.example.donorbox.presentation.di.modules

import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCaseImpl
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadDataUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadDataUseCaseImpl
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDataUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDataUseCaseImpl
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.NotificationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.NotificationUseCaseImpl
import com.example.donorbox.domain.useCase.localDataBaseUseCase.LocalDataBaseUseCase
import com.example.donorbox.domain.useCase.localDataBaseUseCase.LocalDataBaseUseCaseImpl
import com.example.donorbox.domain.useCase.paymentUseCase.PaymentUseCase
import com.example.donorbox.domain.useCase.paymentUseCase.PaymentUseCaseImpl
import com.example.donorbox.domain.useCase.sharedpreferenceUsecase.SharedPreferenceUseCase
import com.example.donorbox.domain.useCase.sharedpreferenceUsecase.SharedPreferenceUseCaseImpl
import org.koin.dsl.module

object UseCaseModule {

    val useCaseModule = module {

        single<AuthenticationUseCase>{
            AuthenticationUseCaseImpl(
                firebaseRepository = get()
            )
        }

        single<FirebaseReadDataUseCase>{
            FirebaseReadDataUseCaseImpl(
                firebaseRepository = get()
            )
        }

        single<FirebaseWriteDataUseCase>{
            FirebaseWriteDataUseCaseImpl(
                firebaseRepository = get()
            )
        }

        single<NotificationUseCase>{
            NotificationUseCaseImpl(
                firebaseRepository = get()
            )
        }

        single<LocalDataBaseUseCase>{
            LocalDataBaseUseCaseImpl(
                localDataBaseRepository = get()
            )
        }

        single<SharedPreferenceUseCase>{
            SharedPreferenceUseCaseImpl(
                sharedPreferencesRepository = get()
            )
        }

        single<PaymentUseCase> {
            PaymentUseCaseImpl(
                paymentRepository = get()
            )
        }
    }
}
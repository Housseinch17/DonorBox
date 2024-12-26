package com.example.donorbox.presentation.di

import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.ChangePasswordUseCase
import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.LogInUseCase
import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.donorbox.domain.useCase.firebaseAuthenticationUseCase.SignUpUseCase
import com.example.donorbox.domain.useCase.firebaseReadDataUseCase.FirebaseReadReceiversUseCase
import com.example.donorbox.domain.useCase.firebaseWriteDataUseCase.FirebaseWriteTokenUseCase
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import org.koin.dsl.module

object UseCaseModule {

    val useCaseModule = module {
        single<ChangePasswordUseCase> {
            ChangePasswordUseCase(get())
        }

        single<GetCurrentUserUseCase> {
            GetCurrentUserUseCase(get())
        }

        single<LogInUseCase> {
            LogInUseCase(get())
        }

        single<ResetPasswordUseCase> {
            ResetPasswordUseCase(get())
        }

        single<SignOutUseCase> {
            SignOutUseCase(get())
        }

        single<SignUpUseCase> {
            SignUpUseCase(get())
        }

        single<FirebaseReadReceiversUseCase> {
            FirebaseReadReceiversUseCase(get())
        }

        single<FirebaseWriteTokenUseCase>{
            FirebaseWriteTokenUseCase(get())
        }

        single<GetSharedPrefUsernameUseCase> {
            GetSharedPrefUsernameUseCase(get())
        }

        single<SaveSharedPrefUsernameUseCase> {
            SaveSharedPrefUsernameUseCase(get())
        }


    }
}
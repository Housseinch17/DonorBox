package com.example.donorbox.presentation.di

import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.ChangePasswordUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.LogInUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.SignUpUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.VerifyPasswordUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseGetAllReceiversUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadReceiversUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteTokenUseCase
import com.example.donorbox.domain.useCase.localDataBaseUseCase.GetAllDonationsUseCase
import com.example.donorbox.domain.useCase.localDataBaseUseCase.SaveDonationsUseCase
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

        single<SaveDonationsUseCase>{
            SaveDonationsUseCase(get())
        }

        single<GetAllDonationsUseCase>{
            GetAllDonationsUseCase(get())
        }

        single<VerifyPasswordUseCase>{
            VerifyPasswordUseCase(get())
        }

        single<FirebaseGetAllReceiversUseCase>{
            FirebaseGetAllReceiversUseCase(get())
        }
    }
}
package com.example.donorbox.presentation.di

import com.example.donorbox.presentation.AuthenticationViewModel
import com.example.donorbox.presentation.screens.login.LogInViewModel
import com.example.donorbox.presentation.screens.main.MainViewModel
import com.example.donorbox.presentation.screens.signup.SignUpViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    val viewModelModule = module {

        viewModel{
            AuthenticationViewModel(
                application = androidApplication(),
                signOutUseCase = get(),
                saveSharedPrefUsernameUseCase = get(),
                getSharedPrefUsernameUseCase = get(),
                resetPasswordUseCase = get(),
                getCurrentUserUseCase = get()
            )
        }

        viewModel{
            MainViewModel(
                getSharedPrefUsernameUseCase = get()
            )
        }
        
        viewModel{
            LogInViewModel(
                logInUseCase = get(),
                saveSharedPrefUsernameUseCase = get(),
                getCurrentUserUseCase = get()
            )
        }

        viewModel{
            SignUpViewModel(
                signUpUseCase = get()
            )
        }
    }
}
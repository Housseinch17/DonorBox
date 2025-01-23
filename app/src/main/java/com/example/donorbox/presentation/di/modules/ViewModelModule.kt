package com.example.donorbox.presentation.di.modules

import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.home.HomeViewModel
import com.example.donorbox.presentation.screens.login.LogInViewModel
import com.example.donorbox.presentation.screens.main.MainViewModel
import com.example.donorbox.presentation.screens.mydonations.MyDonationsViewModel
import com.example.donorbox.presentation.screens.profile.ProfileViewModel
import com.example.donorbox.presentation.screens.receivedDonationsPage.ReceivedDonationsViewModel
import com.example.donorbox.presentation.screens.settings.SettingsViewModel
import com.example.donorbox.presentation.screens.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    val viewModelModule = module {

        viewModel {
            AuthenticationViewModel(
                authenticationUseCase = get(),
                sharedPreferenceUseCase = get(),
                notificationUseCase = get(),
            )
        }

        viewModel {
            MainViewModel(
                sharedPreferenceUseCase = get()
            )
        }

        viewModel {
            LogInViewModel(
                authenticationUseCase = get(),
                sharedPreferenceUseCase = get(),
            )
        }

        viewModel {
            SignUpViewModel(
                authenticationUseCase = get(),
                firebaseWriteDataUseCase = get(),
            )
        }

        viewModel {
            HomeViewModel(
                firebaseReadDataUseCase = get(),
                localDataBaseUseCase = get(),
                authenticationUseCase = get(),
                notificationUseCase = get(),
                firebaseWriteDataUseCase = get(),
            )
        }

        viewModel {
            MyDonationsViewModel(
                localDataBaseUseCase = get()
            )
        }
        viewModel {
            SettingsViewModel(
                authenticationUseCase = get(),
            )
        }

        viewModel {
            ReceivedDonationsViewModel(
                firebaseReadDataUseCase = get()
            )
        }

        viewModel {
            ProfileViewModel(
                authenticationUseCase = get(),
                firebaseReadDataUseCase = get(),
            )
        }

    }
}
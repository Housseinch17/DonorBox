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
                getCurrentUserUseCase = get(),
                updateDeviceTokenUseCase = get(),
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
                signUpUseCase = get(),
                addUserUseCase = get(),
            )
        }

        viewModel {
            HomeViewModel(
                firebaseReadReceiversUseCase = get(),
                saveDonationsUseCase = get(),
                verifyPasswordUseCase = get(),
                sendNotificationToTokenUseCase = get(),
                firebaseReadFullNameUseCase = get(),
                firebaseWriteDonationsUseCase = get(),
            )
        }

        viewModel {
            MyDonationsViewModel(
                getAllDonationsUseCase = get()
            )
        }
        viewModel {
            SettingsViewModel(
                changePasswordUseCase = get(),
                verifyPasswordUseCase = get()
            )
        }

        viewModel {
            ReceivedDonationsViewModel(
                firebaseReadAllDonationsUseCase = get(),)
        }

        viewModel {
            ProfileViewModel(
                getCurrentUserUseCase = get(),
                firebaseReadFullNameUseCase = get()
            )
        }

    }
}
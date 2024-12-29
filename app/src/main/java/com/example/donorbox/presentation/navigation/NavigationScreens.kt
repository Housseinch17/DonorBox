package com.example.donorbox.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    //loading screen
    @Serializable
    data object Loading : NavigationScreens

    //Register Graph
    @Serializable
    data object Register: NavigationScreens

    //Register screens
    @Serializable
    data object LogInPage: NavigationScreens{
        const val ROUTE = "LogInPage"
    }

    @Serializable
    data object SignUpPage: NavigationScreens{
        const val ROUTE = "SignUpPage"
    }

    @Serializable
    data object MyDonationsPage: NavigationScreens

    @Serializable
    data object ReceivedDonationsPage: NavigationScreens

    @Serializable
    data object ProfilePage: NavigationScreens

    @Serializable
    data object SettingsPage: NavigationScreens

    //DonorBox graph
    @Serializable
    data object DonorBox: NavigationScreens

    //DonorBox screens
    @Serializable
    data object HomePage: NavigationScreens

}
package com.example.donorbox.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    //loading screen
    @Serializable
    data object Loading : NavigationScreens

    //Register Graph
    @Serializable
    data object RegisterGraph: NavigationScreens{
        const val ROUTE = "RegisterGraph"
    }

    //Register screens
    @Serializable
    data object LogInPage: NavigationScreens

    @Serializable
    data object SignUpPage: NavigationScreens


    //DonorBox graph
    @Serializable
    data object DonorBoxGraph: NavigationScreens{
        const val ROUTE = "DonorBoxGraph"
    }


    //DonorBox screens
    @Serializable
    data object HomePage: NavigationScreens

    @Serializable
    data object MyDonationsPage: NavigationScreens

    @Serializable
    data object ReceivedDonationsPage: NavigationScreens

    @Serializable
    data object ProfilePage: NavigationScreens

    @Serializable
    data object SettingsPage: NavigationScreens

}
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
    data object LogInPage: NavigationScreens

    @Serializable
    data object SignUpPage: NavigationScreens


    //DonorBox graph
    @Serializable
    data object DonorBox: NavigationScreens

    //DonorBox screens
    @Serializable
    data object HomePage: NavigationScreens

}
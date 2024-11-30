package com.example.donorbox.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.donorbox.presentation.navigation.Navigation
import com.example.donorbox.presentation.navigation.NavigationScreens
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainPage(navController: NavHostController) {
    //initialize viewmodel to check if already logged in via shared preferences
    val mainViewModel = koinViewModel<MainViewModel>()
    val status by mainViewModel.status.collectAsStateWithLifecycle()


    val backStackEntry by navController.currentBackStackEntryAsState()
    //get current screen
    val currentScreenDestination = backStackEntry?.destination?.route
    //get current screen route without (com.example.donorBox...)
    val currentScreenRoute = getScreenName(currentScreenDestination)


    //here if the status is NavigationScreens.Loading it will show the background image set in the theme
    if (status != NavigationScreens.Loading) {
        //use material not material 3 to use docked
        Scaffold { innerPadding ->
            //we  use innerPadding because we added statusBarsPadding() and navigationBarsPadding()
            // to topBar and bottomBar
            //but here i removed topBar so i had to add statusBarsPadding() to show status bar color
            Navigation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navHostController = navController,
                startDestination = status
            )
        }
    }
}


//instead of getting com.example.donorBox.LogInPage it will show LogInPage only
private fun getScreenName(route: String?): String {
    return route?.substringAfterLast('.') ?: ""
}
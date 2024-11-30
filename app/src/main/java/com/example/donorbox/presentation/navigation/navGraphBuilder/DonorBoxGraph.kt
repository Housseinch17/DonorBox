package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.presentation.navigation.NavigationScreens

fun NavGraphBuilder.donorBoxGraph(
    modifier: Modifier,
    navHostController: NavHostController,
){
    navigation<NavigationScreens.DonorBox>(
        startDestination = NavigationScreens.HomePage
    ){
        composable<NavigationScreens.HomePage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.DonorBox)

        }
    }
}
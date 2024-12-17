package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.home.HomePage
import com.example.donorbox.presentation.screens.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.donorBoxGraph(
    navHostController: NavHostController,
) {
    navigation<NavigationScreens.DonorBox>(
        startDestination = NavigationScreens.HomePage
    ) {
        composable<NavigationScreens.HomePage> {
            val context = LocalContext.current
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.DonorBox)
            val homeViewModel =
                koinViewModel<HomeViewModel>(viewModelStoreOwner = parentBackStackEntry)
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(homeViewModel.sharedFlow) {
                homeViewModel.sharedFlow.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            HomePage(
                modifier = Modifier.fillMaxSize(),
                response = uiState.receiversResponse
            ) {
                Log.d("MyTag", "$it")
            }
        }
    }
}
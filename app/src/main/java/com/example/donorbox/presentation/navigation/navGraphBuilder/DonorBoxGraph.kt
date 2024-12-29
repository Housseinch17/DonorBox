package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.home.HomePage
import com.example.donorbox.presentation.screens.home.HomeViewModel
import com.example.donorbox.presentation.util.callPhoneDirectly
import com.example.donorbox.presentation.util.openApp
import com.example.donorbox.presentation.util.openGoogleMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

            val scope = rememberCoroutineScope()

            LaunchedEffect(homeViewModel.sharedFlow) {
                homeViewModel.sharedFlow.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            HomePage(
                modifier = Modifier.fillMaxSize(),
                response = uiState.receiversResponse,
                onReceiverClick = {
                    homeViewModel.updateModalBottomSheetReceiver(it)
                    homeViewModel.showBottomSheetReceiver()
                },
                modalBottomSheetReceiver = uiState.modalBottomSheetReceiver,
                hideBottomSheetReceiver = homeViewModel::hideBottomSheetReceiver,
                onCall = { phoneNumber ->
                    context.callPhoneDirectly(phoneNumber) {
                        Toast.makeText(context, "Permission not granted!", Toast.LENGTH_LONG).show()
                    }
                },
                onOpenApp = {
                    context.openApp(packageName = "com.tedmob.omt")
                },
                onOpenWhishApp = {
                    context.openApp(packageName = "money.whish.android")
                },
                onOpenGoogleMap = { latitude, longitude ->
                    context.openGoogleMap(latitude, longitude)
                },
                onSendButton = homeViewModel::showDialog,
                sendMoney = {
                    homeViewModel.updateLoader(true)
                    scope.launch {
                        homeViewModel.saveDonations(
                            donations = MyDonations(
                                myDonations = "Donated $it$  to: ${uiState.modalBottomSheetReceiver.modalBottomSheetReceiver.name}"
                            )
                        )
                        delay(2000)
                        homeViewModel.updateLoader(false)
                        homeViewModel.updateMoneyToDonate("")
                        homeViewModel.hideDialog()
                    }
                },
                showDialog = uiState.dialogVisibility,
                hideDialog = homeViewModel::hideDialog,
                moneyToDonate = uiState.moneyToDonate,
                onMoneyUpdate = {
                    homeViewModel.updateMoneyToDonate(it)
                },
                isLoading = uiState.isLoading
            )
        }

        composable<NavigationScreens.MyDonationsPage> {

        }
        composable<NavigationScreens.ReceivedDonationsPage> {

        }
        composable<NavigationScreens.SettingsPage> {

        }
        composable<NavigationScreens.ProfilePage> {

        }

        composable<NavigationScreens.ReceivedDonationsPage> {

        }
    }
}
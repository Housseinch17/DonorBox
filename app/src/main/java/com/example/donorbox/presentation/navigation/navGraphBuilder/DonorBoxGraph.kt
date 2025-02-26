package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.R
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.authentication.AuthenticationUiState
import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.contactUs.ContactUsPage
import com.example.donorbox.presentation.screens.contactUs.ContactUsViewModel
import com.example.donorbox.presentation.screens.home.HomePage
import com.example.donorbox.presentation.screens.home.HomeViewModel
import com.example.donorbox.presentation.screens.home.PaymentStatus
import com.example.donorbox.presentation.screens.mydonations.MyDonationPage
import com.example.donorbox.presentation.screens.mydonations.MyDonationsViewModel
import com.example.donorbox.presentation.screens.profile.ProfilePage
import com.example.donorbox.presentation.screens.profile.ProfileViewModel
import com.example.donorbox.presentation.screens.receivedDonationsPage.ReceivedDonationsPage
import com.example.donorbox.presentation.screens.receivedDonationsPage.ReceivedDonationsViewModel
import com.example.donorbox.presentation.screens.settings.SettingsPage
import com.example.donorbox.presentation.screens.settings.SettingsViewModel
import com.example.donorbox.presentation.util.navigateSingleTopTo
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RestrictedApi", "UnrememberedGetBackStackEntry")
fun NavGraphBuilder.donorBoxGraph(
    navHostController: NavHostController,
    authenticationUiState: AuthenticationUiState,
    authenticationViewModel: AuthenticationViewModel,
) {
    val modifier: Modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(horizontal = 10.dp)

    navigation<NavigationScreens.DonorBoxGraph>(startDestination = NavigationScreens.HomePage) {
        composable<NavigationScreens.HomePage> {
            val context = LocalContext.current
            Log.d("BackStack", "${navHostController.currentBackStack.value}")
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val homeViewModel =
                koinViewModel<HomeViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(homeViewModel.eventMessage) {
                homeViewModel.eventMessage.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            LaunchedEffect(homeViewModel.paymentStatus) {
                homeViewModel.paymentStatus.collect { paymentStatus ->
                    when (paymentStatus) {
                        PaymentStatus.Canceled -> {
                            Toast.makeText(context, "Payment Canceled!", Toast.LENGTH_LONG).show()
                        }

                        is PaymentStatus.Failed -> {
                            Toast.makeText(
                                context,
                                "Payment Failed: ${paymentStatus.errorMessage}!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
            HomePage(
                modifier = modifier,
                homeUiState = homeUiState,
                onActionHomeAction = homeViewModel::onActionHome,
                onActionAuthenticationAction = authenticationViewModel::onActionAuthentication
            )
        }
        composable<NavigationScreens.MyDonationsPage> {
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val myDonationsViewModel =
                koinViewModel<MyDonationsViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val myDonationsUiState by myDonationsViewModel.myDonationsUiState.collectAsStateWithLifecycle()

            MyDonationPage(
                modifier = modifier,
                myDonationsUiState = myDonationsUiState,
            )
        }
        composable<NavigationScreens.ReceivedDonationsPage> {
            Log.d("BackStack", "${navHostController.currentBackStack.value}")
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val receivedDonationsViewModel =
                koinViewModel<ReceivedDonationsViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val receivedDonationsUiState by receivedDonationsViewModel.receivedDonationsUiState.collectAsStateWithLifecycle()

            ReceivedDonationsPage(
                modifier = modifier,
                receivedDonationsUiState = receivedDonationsUiState,
                onActionReceivedDonationsAction = receivedDonationsViewModel::onActionReceivedDonations,
            )

        }
        composable<NavigationScreens.SettingsPage> {
            Log.d("BackStack", "${navHostController.currentBackStack.value}")
            val context = LocalContext.current
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val settingsViewModel =
                koinViewModel<SettingsViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()


            LaunchedEffect(settingsViewModel.eventMessage) {
                settingsViewModel.eventMessage.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            SettingsPage(
                modifier = modifier,
                settingsUiState = settingsUiState,
                onActionSettings = settingsViewModel::onActionSettings,
                authenticationUiState = authenticationUiState,
                onActionAuthentication = authenticationViewModel::onActionAuthentication,
                textPage = stringResource(R.string.settings),

                )
        }

        composable<NavigationScreens.ProfilePage> {
            Log.d("BackStack", "${navHostController.currentBackStack.value}")
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val profileViewModel =
                koinViewModel<ProfileViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()

            ProfilePage(
                modifier = modifier,
                profileUiState = profileUiState,
                contactUs = {
                    navHostController.navigateSingleTopTo(
                        route = NavigationScreens.ContactUs,
                        navHostController = navHostController
                    )
                }
            )
        }

        composable<NavigationScreens.ContactUs> {
            Log.d("BackStack", "${navHostController.currentBackStack.value}")
            val donorBoxBackStackEntry =
                remember(navHostController) {
                    navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
                }
            val contactUsViewModel =
                koinViewModel<ContactUsViewModel>(viewModelStoreOwner = donorBoxBackStackEntry)
            val contactUsUiState by contactUsViewModel.contactUsUiState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(contactUsViewModel.eventMessage) {
                contactUsViewModel.eventMessage.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            ContactUsPage(
                modifier = modifier,
                contactUsUiState = contactUsUiState,
                onActionContactUs = contactUsViewModel::onActionContactUs,
                textPage = stringResource(R.string.contact_us)
            )

        }
    }
}
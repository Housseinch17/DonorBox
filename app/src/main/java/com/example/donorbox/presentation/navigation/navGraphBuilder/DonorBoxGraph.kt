package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.R
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.authentication.AuthenticationUiState
import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.home.HomePage
import com.example.donorbox.presentation.screens.home.HomeViewModel
import com.example.donorbox.presentation.screens.mydonations.MyDonationPage
import com.example.donorbox.presentation.screens.mydonations.MyDonationsViewModel
import com.example.donorbox.presentation.screens.profile.ProfilePage
import com.example.donorbox.presentation.screens.profile.ProfileViewModel
import com.example.donorbox.presentation.screens.receivedDonationsPage.ReceivedDonationsPage
import com.example.donorbox.presentation.screens.receivedDonationsPage.ReceivedDonationsViewModel
import com.example.donorbox.presentation.screens.settings.SettingsPage
import com.example.donorbox.presentation.screens.settings.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

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

            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
            val homeViewModel =
                koinViewModel<HomeViewModel>(viewModelStoreOwner = parentBackStackEntry)
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(homeViewModel.eventMessage) {
                homeViewModel.eventMessage.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
            val myDonationsViewModel = koinViewModel<MyDonationsViewModel>()
            val myDonationsUiState by myDonationsViewModel.myDonationsUiState.collectAsStateWithLifecycle()

            MyDonationPage(
                modifier = modifier,
                myDonationsUiState = myDonationsUiState,
                onActionMyDonations = myDonationsViewModel::onActionMyDonations,
            )
        }
        composable<NavigationScreens.ReceivedDonationsPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val receivedDonationsViewModel = koinViewModel<ReceivedDonationsViewModel>()
            val receivedDonationsUiState by receivedDonationsViewModel.receivedDonationsUiState.collectAsStateWithLifecycle()

            ReceivedDonationsPage(
                modifier = modifier,
                receivedDonationsUiState = receivedDonationsUiState,
                onActionReceivedDonationsAction = receivedDonationsViewModel::onActionReceivedDonations,
            )

        }
        composable<NavigationScreens.SettingsPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()


            LaunchedEffect(settingsViewModel.eventMessage) {
                settingsViewModel.eventMessage.collectLatest { message ->
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
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val profileViewModel = koinViewModel<ProfileViewModel>()
            val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()

            ProfilePage(
                modifier = modifier,
                profileUiState = profileUiState
            )
        }
    }
}
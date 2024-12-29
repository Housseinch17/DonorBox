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
import com.example.donorbox.presentation.AuthenticationViewModel
import com.example.donorbox.presentation.ResetPage
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.home.HomePage
import com.example.donorbox.presentation.screens.home.HomeViewModel
import com.example.donorbox.presentation.screens.mydonations.MyDonationPage
import com.example.donorbox.presentation.screens.mydonations.MyDonationsViewModel
import com.example.donorbox.presentation.screens.settings.SettingsPage
import com.example.donorbox.presentation.screens.settings.SettingsViewModel
import com.example.donorbox.presentation.util.callPhoneDirectly
import com.example.donorbox.presentation.util.openApp
import com.example.donorbox.presentation.util.openGoogleMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.donorBoxGraph(
    navHostController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    username: String,
    resetShowDialog: Boolean,
    resetIsLoading: Boolean,
    signOutShowDialog: Boolean,
    signOutIsLoading: Boolean
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
                        homeViewModel.saveDonations(moneyToDonate = it,
                            donations = MyDonations(
                                myDonations = "Donated $it$  to: ${uiState.modalBottomSheetReceiver.modalBottomSheetReceiver.name}"
                            )
                        )
                        homeViewModel.updateLoader(false)
                        homeViewModel.updateMoneyToDonate("")
                    }
                },
                showDialog = uiState.dialogVisibility,
                hideDialog = homeViewModel::hideDialog,
                moneyToDonate = uiState.moneyToDonate,
                onMoneyUpdate = {
                    homeViewModel.updateMoneyToDonate(it)
                },
                isLoading = uiState.isLoading,
                showText =  uiState.showText
            )
        }

        composable<NavigationScreens.MyDonationsPage> {
            val myDonationsViewModel = koinViewModel<MyDonationsViewModel>()
            val myDonationsUiState by myDonationsViewModel.myDonationsUiState.collectAsStateWithLifecycle()

            MyDonationPage(
                isLoading = myDonationsUiState.isLoading,
                list = myDonationsUiState.list,
                isRefreshing = myDonationsUiState.isRefreshing,
                onRefresh = myDonationsViewModel::loadNewOrders
            )
        }
        composable<NavigationScreens.ReceivedDonationsPage> {

        }
        
        composable<NavigationScreens.SettingsPage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            LaunchedEffect(settingsViewModel.emitValue) {
                settingsViewModel.emitValue.collectLatest { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            SettingsPage(
                modifier = Modifier.fillMaxSize(),
                newPasswordValue = settingsUiState.newPasswordValue,
                confirmPasswordValue = settingsUiState.confirmNewPasswordValue,
                newPasswordValueChange = { newPassword ->
                    settingsViewModel.newPasswordValueChange(newPassword)
                },
                confirmPasswordValueChange = { confirmPassword ->
                    settingsViewModel.confirmNewPasswordValueChange(confirmPassword)
                },
                imageVector = settingsViewModel.getIconVisibility(
                    settingsUiState.showPassword
                ),
                confirmImageVector = settingsViewModel.getIconVisibility(settingsUiState.confirmShowPassword),
                onIconClick = settingsViewModel::setShowPassword,
                confirmOnIconClick = settingsViewModel::setConfirmShowPassword,
                showPassword = settingsUiState.showPassword,
                confirmShowPassword = settingsUiState.confirmShowPassword,
                showText = settingsUiState.showText,
                confirmShowText = settingsUiState.confirmShowText,
                onPasswordChange = { newPassword, confirmPassword ->
                    settingsViewModel.changePassword(
                        email = username,
                        newPassword = newPassword,
                        confirmNewPassword = confirmPassword
                    )
                },
                resetShowDialog = resetShowDialog,
                resetPassword = {
                    Log.d("MyTag", "hey")
                    authenticationViewModel.resetPassword(
                        email = "",
                        resetPage = ResetPage.SettingsPage
                    )
                },
                resetDismiss = authenticationViewModel::resetResetHideDialog,
                resetIsLoading = resetIsLoading,
                onResetPassword = authenticationViewModel::resetResetShowDialog,
                onSignOut = authenticationViewModel::resetShowDialog,
                signOutShowDialog = signOutShowDialog,
                signOutConfirm = authenticationViewModel::signOut,
                signOutDismiss = authenticationViewModel::resetHideDialog,
                signOutIsLoading = signOutIsLoading,
            )
        }

        composable<NavigationScreens.ProfilePage> {

        }

        composable<NavigationScreens.ReceivedDonationsPage> {

        }
    }
}
package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.authentication.ResetPage
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
import com.example.donorbox.presentation.screens.settings.ShowPassword
import com.example.donorbox.presentation.screens.settings.UpdatePassword
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
    val modifier: Modifier = Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 10.dp)

    navigation<NavigationScreens.DonorBoxGraph>(startDestination = NavigationScreens.HomePage) {
        composable<NavigationScreens.HomePage> {
            val context = LocalContext.current

            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.DonorBoxGraph)
            val homeViewModel =
                koinViewModel<HomeViewModel>(viewModelStoreOwner = parentBackStackEntry)
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(homeViewModel.sharedFlow) {
                homeViewModel.sharedFlow.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            HomePage(
                modifier = modifier,
                homeUiState = homeUiState,
                onActionHomeAction = homeViewModel::onActionHome,
            )
        }
        composable<NavigationScreens.MyDonationsPage> {
            val myDonationsViewModel = koinViewModel<MyDonationsViewModel>()
            val myDonationsUiState by myDonationsViewModel.myDonationsUiState.collectAsStateWithLifecycle()

            MyDonationPage(
                modifier = modifier,
                isLoading = myDonationsUiState.isLoading,
                list = myDonationsUiState.list,
                isRefreshing = myDonationsUiState.isRefreshing,
                onRefresh = myDonationsViewModel::loadNewOrders
            )
        }
        composable<NavigationScreens.ReceivedDonationsPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val receivedDonationsViewModel = koinViewModel<ReceivedDonationsViewModel>()
            val receivedDonationsUiState by receivedDonationsViewModel.receivedDonationsUiState.collectAsStateWithLifecycle()

            ReceivedDonationsPage(
                modifier = modifier,
                isLoading = receivedDonationsUiState.isLoading,
                isRefreshing = receivedDonationsUiState.isRefreshing,
                receivedDonationsList = receivedDonationsUiState.receivedDonationsList,
                onRefresh = receivedDonationsViewModel::loadNewOrders
            )

        }
        composable<NavigationScreens.SettingsPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

            LaunchedEffect(settingsViewModel.emitValue) {
                settingsViewModel.emitValue.collectLatest { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            SettingsPage(
                modifier = modifier,
                textPage = stringResource(R.string.settings),
                currentPasswordValue = settingsUiState.currentPasswordValue,
                newPasswordValue = settingsUiState.newPasswordValue,
                confirmPasswordValue = settingsUiState.confirmNewPasswordValue,
                currentPasswordValueChange = { currentPassword ->
                    settingsViewModel.updatePassword(
                        UpdatePassword.CurrentPassword(
                            currentPassword
                        )
                    )
                },
                newPasswordValueChange = { newPassword ->
                    settingsViewModel.updatePassword(UpdatePassword.NewPassword(newPassword))
                },
                confirmPasswordValueChange = { confirmPassword ->
                    settingsViewModel.updatePassword(
                        UpdatePassword.ConfirmPassword(
                            confirmPassword
                        )
                    )
                },
                currentPasswordImageVector = settingsViewModel.getIconVisibility(
                    settingsUiState.currentShowPassword
                ),
                newImageVector = settingsViewModel.getIconVisibility(
                    settingsUiState.newShowPassword
                ),
                confirmImageVector = settingsViewModel.getIconVisibility(settingsUiState.confirmShowPassword),
                currentPasswordOnIconClick = { settingsViewModel.showPassword(ShowPassword.CurrentPassword) },
                newOnIconClick = { settingsViewModel.showPassword(ShowPassword.NewPassword) },
                confirmOnIconClick = { settingsViewModel.showPassword(ShowPassword.ConfirmPassword) },
                currentShowPassword = settingsUiState.currentShowPassword,
                newShowPassword = settingsUiState.newShowPassword,
                confirmShowPassword = settingsUiState.confirmShowPassword,
                showText = settingsUiState.showText,
                confirmShowText = settingsUiState.confirmShowText,
                onPasswordChange = { currentPassword, newPassword, confirmPassword ->
                    scope.launch {
                        settingsViewModel.verifyPassword(currentPassword,
                            onVerified = {
                                settingsViewModel.changePassword(
                                    email = username,
                                    newPassword = newPassword,
                                    confirmNewPassword = confirmPassword
                                )
                            },
                            setError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            })
                    }

                },
                resetShowDialog = resetShowDialog,
                resetPassword = {
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
                isLoading = settingsUiState.isLoading
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
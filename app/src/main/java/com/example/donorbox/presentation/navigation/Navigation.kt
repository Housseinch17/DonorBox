package com.example.donorbox.presentation.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.donorbox.presentation.AuthenticationViewModel
import com.example.donorbox.presentation.SignOutResponse
import com.example.donorbox.presentation.navigation.navGraphBuilder.donorBoxGraph
import com.example.donorbox.presentation.navigation.navGraphBuilder.registerGraph
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController,
    startDestination: NavigationScreens
){
    val authenticationViewModel = koinViewModel<AuthenticationViewModel>()
    val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()

    val signOut = authenticationUiState.signOut

    val currentUsername = authenticationUiState.username

    LaunchedEffect(Unit) {
        Log.d("MyTag", currentUsername.toString())
    }

    val context = LocalContext.current

    LaunchedEffect(signOut) {
        when (signOut) {
            SignOutResponse.Success -> {
                authenticationViewModel.resetSignOutState()
                navHostController.navigate(NavigationScreens.LogInPage) {
                    //popUpTo(0) here 0 means we will remove all the old stacks in BackStackEntry
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(authenticationViewModel.showMessage) {
        authenticationViewModel.showMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ){
        registerGraph(
            authenticationViewModel = authenticationViewModel,
            navHostController = navHostController,
            onResetEmailValue = authenticationUiState.resetEmailValue,
            resetShowDialog = authenticationUiState.resetShowDialog,
            email = authenticationUiState.resetEmailValue,
            resetIsLoading = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
        )

        donorBoxGraph(
            navHostController = navHostController,
            authenticationViewModel = authenticationViewModel,
            username = currentUsername ?: "",
            resetShowDialog = authenticationUiState.resetShowDialog,
            resetIsLoading = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
            signOutShowDialog = authenticationUiState.signOutShowDialog,
            signOutIsLoading = authenticationUiState.signOut == SignOutResponse.IsLoading,
        )
    }
}
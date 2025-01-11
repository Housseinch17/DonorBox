package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.R
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.authentication.AuthenticationUiState
import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.login.LogInScreen
import com.example.donorbox.presentation.screens.login.LogInViewModel
import com.example.donorbox.presentation.screens.signup.SignUpScreen
import com.example.donorbox.presentation.screens.signup.SignUpViewModel
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.registerGraph(
    authenticationViewModel: AuthenticationViewModel,
    authenticationUiState: AuthenticationUiState,
    navHostController: NavHostController,
) {
    navigation<NavigationScreens.RegisterGraph>(
        startDestination = NavigationScreens.LogInPage
    ) {
        composable<NavigationScreens.LogInPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val logInViewModel = koinViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            LaunchedEffect(logInViewModel.sharedFlow) {
                logInViewModel.sharedFlow.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            LaunchedEffect(logInUiState.authState) {
                when (logInUiState.authState) {
                    AuthState.LoggedIn -> {
                        //update currentUsername before navigating
                        authenticationViewModel.updateCurrentUserName()
                        navHostController.navigate(NavigationScreens.HomePage) {
                            //remove LogInPage from currentBackStack
                            popUpTo(NavigationScreens.LogInPage) {
                                inclusive = true
                            }
                        }
                    }

                    else -> {}
                }
            }
            LogInScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 20.dp),
                textPage = stringResource(R.string.hello_sign_in),
                logInUiState = logInUiState,
                onActionLogIn = logInViewModel::onActionLogIn,
                onActionAuthentication = authenticationViewModel::onActionAuthentication,
                authenticationUiState = authenticationUiState,
                onSignUpClick = {
                    navHostController.navigate(NavigationScreens.SignUpPage)
                },
            )
        }

        composable<NavigationScreens.SignUpPage> {
            Log.d("BackStack", "${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val signUpViewModel = koinViewModel<SignUpViewModel>()
            val signUpUiState by signUpViewModel.signupUiState.collectAsStateWithLifecycle()


            LaunchedEffect(signUpViewModel.signUpSharedFlow) {
                signUpViewModel.signUpSharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(signUpUiState.accountStatus) {
                Log.d("MyTag", "launch")
                when (signUpUiState.accountStatus) {
                    is AccountStatus.IsCreated -> {
                        Log.d("MyTag", "IsCreated")
                        navHostController.navigate(NavigationScreens.LogInPage)
                    }

                    else -> {
                        Log.d(
                            "MyTag", signUpUiState.accountStatus.toString()
                        )
                    }
                }
            }

            //set delay for signup button to avoid spamming it
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> signUpViewModel.setAlreadyHaveAccountButton()
                        else -> {}
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            SignUpScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 20.dp)
                    .padding(top = 20.dp),
                textPage = stringResource(R.string.create_your_account),
                signUpUiState = signUpUiState,
                signUpAction = signUpViewModel::onActionSignUp,
                onExistingAccount = {
                    navHostController.navigateUp()
                },
            )
        }
    }
}
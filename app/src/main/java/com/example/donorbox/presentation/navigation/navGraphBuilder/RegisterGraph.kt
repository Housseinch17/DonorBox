package com.example.donorbox.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.donorbox.R
import com.example.donorbox.presentation.AuthenticationViewModel
import com.example.donorbox.presentation.ResetPage
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.login.LogInScreen
import com.example.donorbox.presentation.screens.login.LogInViewModel
import com.example.donorbox.presentation.screens.signup.SignUpScreen
import com.example.donorbox.presentation.screens.signup.SignUpViewModel
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.registerGraph(
    authenticationViewModel: AuthenticationViewModel,
    navHostController: NavHostController,
    onResetEmailValue: String,
    resetShowDialog: Boolean,
    email: String,
    resetIsLoading: Boolean,
){
    navigation<NavigationScreens.Register>(
        startDestination = NavigationScreens.LogInPage
    ){
        composable<NavigationScreens.LogInPage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val logInViewModel = koinViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

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
                modifier = Modifier.fillMaxSize(),
                textPage = stringResource(R.string.welcome_back),
                emailValue = logInUiState.emailValue,
                onEmailChange = { newEmail ->
                    logInViewModel.setEmail(newEmail)
                },
                imageVector = logInViewModel.getIconVisibility(),
                onIconClick = logInViewModel::setShowPassword,
                showPassword = logInUiState.showPassword,
                passwordValue = logInUiState.passwordValue,
                onPasswordChange = { newPassword ->
                    logInViewModel.setPassword(newPassword)
                },
                "LogIn",
                "Don't have an account, SignUp!",
                logInEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                signUpEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                isLoading = logInUiState.isLoading,
                onLogInClick = { email, password ->
                    scope.launch {
                        logInViewModel.logIn(email, password)
                    }
                },
                onSignUpClick = {
                    navHostController.navigate(NavigationScreens.SignUpPage)
                },
                onResetEmailValue = onResetEmailValue,
                onResetEmailChange = { newEmail ->
                    authenticationViewModel.onResetEmailValue(newEmail)
                },
                resetShowDialog = resetShowDialog,
                resetPassword = {
                    authenticationViewModel.resetPassword(
                        email = email,
                        resetPage = ResetPage.LogInPage
                    )
                },
                resetDismiss = authenticationViewModel::resetResetHideDialog,
                resetIsLoading = resetIsLoading,
                resetPasswordEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                onResetPassword = authenticationViewModel::resetResetShowDialog
            )
        }


        composable<NavigationScreens.SignUpPage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
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
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
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

            SignUpScreen(Modifier.fillMaxSize(),
                textPage = "SignUp Page",
                emailValue = signUpUiState.email,
                onEmailChange = { newEmail ->
                    signUpViewModel.setEmail(newEmail)
                },
                imageVector = signUpViewModel.getIconVisibility(),
                onIconClick = signUpViewModel::setShowPassword,
                showPassword = signUpUiState.showPassword,
                passwordValue = signUpUiState.password,
                onPasswordChange = { newPassword ->
                    signUpViewModel.setPassword(newPassword)
                },
                buttonText = "Create Account",
                accountTextButton = "Already have an account? Login!",
                createAccountEnabled = signUpUiState.accountStatus == AccountStatus.NotCreated,
                alreadyExistingEnabled = signUpUiState.alreadyHaveAccountButton && (signUpUiState.accountStatus == AccountStatus.NotCreated),
                onCreateAccount = { email, password ->
                    signUpViewModel.signUp(email, password)
                },
                onExistingAccount = {
                    navHostController.navigateUp()
                })
        }

    }
}
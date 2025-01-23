package com.example.donorbox.presentation.screens.authentication

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.NotificationUseCase
import com.example.donorbox.domain.useCase.sharedpreferenceUsecase.SharedPreferenceUseCase
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.util.isInternetAvailable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed interface PasswordChangement {
    data class Success(val successMessage: String) : PasswordChangement
    data class Error(val errorMessage: String) : PasswordChangement
    data object InitialState : PasswordChangement
    data object IsLoading : PasswordChangement
}

sealed interface SignOutResponse {
    data object IsLoading : SignOutResponse
    data object InitialState : SignOutResponse
    data object Success : SignOutResponse
    data object Error : SignOutResponse
}

sealed interface ResetPage {
    data object LogInPage : ResetPage
    data object SettingsPage : ResetPage
}

sealed interface AuthenticationAction {
    data class OnResetEmailChange(val emailValue: String) : AuthenticationAction
    data class ResetPassword(val emailValue: String, val resetPage: ResetPage) :
        AuthenticationAction

    data object ResetDismiss : AuthenticationAction
    data object OnResetPassword : AuthenticationAction
    data object ResetSignOutState : AuthenticationAction
    data object ResetShowDialog : AuthenticationAction
    data object ResetHideDialog : AuthenticationAction
    data object SignOut : AuthenticationAction
    data object BottomBarLoading : AuthenticationAction

}

class AuthenticationViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
    private val notificationUseCase: NotificationUseCase
) : ViewModel(), KoinComponent {

    private val application: Application by inject()

    private val _authenticationUiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState())
    val authenticationUiState: StateFlow<AuthenticationUiState> =
        _authenticationUiState.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()


    init {
        Log.d("ViewModelInitialization", "Authentication created")
        viewModelScope.launch {
            updateCurrentUserName()
            updateTokenIntoFirebase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "authentication destroyed")
    }


    fun onActionAuthentication(authenticationAction: AuthenticationAction) {
        when (authenticationAction) {
            AuthenticationAction.ResetDismiss -> resetResetHideDialog()
            is AuthenticationAction.OnResetEmailChange -> onResetEmailValue(authenticationAction.emailValue)
            AuthenticationAction.OnResetPassword -> resetResetShowDialog()
            is AuthenticationAction.ResetPassword -> resetPassword(
                email = authenticationAction.emailValue,
                resetPage = authenticationAction.resetPage
            )

            AuthenticationAction.ResetSignOutState -> resetSignOutState()
            AuthenticationAction.ResetShowDialog -> resetShowDialog()
            AuthenticationAction.ResetHideDialog -> resetHideDialog()
            AuthenticationAction.SignOut -> signOut()
            AuthenticationAction.BottomBarLoading -> bottomBarLoading()
        }
    }

    private fun bottomBarLoading() {
        _authenticationUiState.update { newState ->
            newState.copy(bottomBarLoading = false)
        }
    }


    fun updateCurrentScreen(currentScreen: NavigationScreens) {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(currentScreen = currentScreen)
            }
        }
    }

    private suspend fun updateTokenIntoFirebase() {
        notificationUseCase.updateDeviceToken()
    }


    private fun emitMessage(message: String = "No internet connection") {
        viewModelScope.launch {
            _eventMessage.send(message)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateCurrentUserName() {
        val currentUsername = sharedPreferenceUseCase.getUsername()
        _authenticationUiState.update { newState ->
            newState.copy(username = currentUsername)
        }
    }

    private fun resetResetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = false)
            }
        }
    }

    private fun onResetEmailValue(email: String) {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetEmailValue = email)
            }
        }
    }

    private fun resetResetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = true)
            }
        }
    }

    private fun resetPassword(email: String = "", resetPage: ResetPage) {
        viewModelScope.launch {
            if ((resetPage is ResetPage.LogInPage && Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()) || resetPage is ResetPage.SettingsPage
            ) {
                _authenticationUiState.update { newState ->
                    newState.copy(resetPassword = PasswordChangement.IsLoading)
                }

                val resetPassword: PasswordChangement = when (resetPage) {
                    ResetPage.LogInPage -> authenticationUseCase.resetPassword(email)
                    ResetPage.SettingsPage -> authenticationUseCase.resetPassword(
                        authenticationUseCase.getCurrentUser()!!
                    )
                }
                _authenticationUiState.update { newState ->
                    newState.copy(
                        resetPassword = resetPassword,
                        resetShowDialog = false
                    )
                }
                when (resetPassword) {
                    is PasswordChangement.Error -> emitMessage(resetPassword.errorMessage)
                    is PasswordChangement.Success -> {
                        emitMessage(resetPassword.successMessage)
                        _authenticationUiState.update { newState ->
                            newState.copy(resetEmailValue = "")
                        }
                    }

                    else -> {
                        emitMessage("Check if email exists!")
                    }
                }
                Log.d("MyTag", "resetPassword() ${_authenticationUiState.value.resetPassword}")
            } else {
                emitMessage("Email not valid")
            }
        }
    }


    //reset state while signing out
    fun resetSignOutState() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.InitialState)
            }
        }
    }

    private fun resetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = true)
            }
        }
    }

    private fun resetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = false)
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.IsLoading)
            }
            val hasInternet = application.isInternetAvailable()
            Log.d("hasInternet", "$hasInternet")
            if (hasInternet) {
                authenticationUseCase.signOut()
                _authenticationUiState.update { newState ->
                    newState.copy(
                        signOut = SignOutResponse.Success,
                        signOutShowDialog = false
                    )
                }
                sharedPreferenceUseCase.saveUsername(null)
            } else {
                _authenticationUiState.update { newState ->
                    newState.copy(
                        signOut = SignOutResponse.Error,
                        signOutShowDialog = false
                    )
                }
                //show error to the user
                emitMessage()
            }
        }
    }


}

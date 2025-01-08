package com.example.donorbox.presentation.screens.authentication

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.UpdateDeviceTokenUseCase
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.util.isInternetAvailable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    application: Application,
    private val signOutUseCase: SignOutUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase
) : AndroidViewModel(application = application) {
    private val _authenticationUiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState())
    val authenticationUiState: StateFlow<AuthenticationUiState> =
        _authenticationUiState.asStateFlow()

    private val _showMessage = MutableSharedFlow<String>()
    val showMessage = _showMessage.asSharedFlow()


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


    private suspend fun updateTokenIntoFirebase(){
        updateDeviceTokenUseCase.updateDeviceToken()
    }


    private fun emitMessage(message: String = "No internet connection") {
        viewModelScope.launch {
            _showMessage.emit(message)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateCurrentUserName() {
        val currentUsername = getSharedPrefUsernameUseCase.getUsername()
        _authenticationUiState.update { newState ->
            newState.copy(username = currentUsername)
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

    fun resetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = true)
            }
        }
    }

    fun resetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = false)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.IsLoading)
            }
            val hasInternet = getApplication<Application>().isInternetAvailable()
            Log.d("hasInternet", "$hasInternet")
            if (hasInternet) {
                signOutUseCase.signOut()
                _authenticationUiState.update { newState ->
                    newState.copy(
                        signOut = SignOutResponse.Success,
                        signOutShowDialog = false
                    )
                }
                saveSharedPrefUsernameUseCase.saveUsername(null)
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

    fun resetResetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = true)
            }
        }
    }

    fun resetResetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = false)
            }
        }
    }

    fun onResetEmailValue(email: String) {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetEmailValue = email)
            }
        }
    }

    fun resetPassword(email: String = "", resetPage: ResetPage) {
        viewModelScope.launch {
            if ((resetPage is ResetPage.LogInPage && Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()) || resetPage is ResetPage.SettingsPage
            ) {
                _authenticationUiState.update { newState ->
                    newState.copy(resetPassword = PasswordChangement.IsLoading)
                }

                val resetPassword: PasswordChangement = when (resetPage) {
                    ResetPage.LogInPage -> resetPasswordUseCase.resetPassword(email)
                    ResetPage.SettingsPage -> resetPasswordUseCase.resetPassword(
                        getCurrentUserUseCase.getCurrentUser()!!
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
                Log.d("MyTag","resetPassword() ${_authenticationUiState.value.resetPassword}" )
            } else {
                emitMessage("Email not valid")
            }
        }
    }
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
package com.example.donorbox.presentation.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.LogInUseCase
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface LogInAction{
    data class SetEmail(val email: String): LogInAction
    data class OnPasswordChange(val passwordValue: String): LogInAction
    data class OnLogIn(val emailValue: String, val passwordValue: String): LogInAction
    data object SetShowPassword: LogInAction
}


class LogInViewModel(
    private val logInUseCase: LogInUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    private val _sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()


    init {
        Log.d("ViewModelInitialization","LoginViewModel created")
        showLoader()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","LoginViewModel destroyed")
    }

    fun onActionLogIn(logInAction: LogInAction){
        when(logInAction){
            is LogInAction.SetEmail -> setEmail(email = logInAction.email)
            LogInAction.SetShowPassword -> setShowPassword()
            is LogInAction.OnPasswordChange -> setPassword(password = logInAction.passwordValue)
            is LogInAction.OnLogIn -> logIn(email = logInAction.emailValue, password = logInAction.passwordValue)
        }
    }

    private fun showLoader(){
        viewModelScope.launch {
            _logInUiState.update { newState->
                newState.copy(isLoading = true)
            }
            delay(500L)
            _logInUiState.update { newState->
                newState.copy(isLoading = false)
            }
        }
    }

    private fun emitSharedFlow(message: String) {
        viewModelScope.launch {
            _sharedFlow.emit(message)
        }
    }


    private fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(authState = AuthState.Loading)
            }
            if (email.isEmpty() || password.isEmpty()) {
                emitSharedFlow("Email and Password can't be empty")
                _logInUiState.update { newState ->
                    newState.copy(authState = AuthState.NotLoggedIn)
                }
            } else {
                val response = logInUseCase.logIn(email, password)
                if (response is AuthState.Error) {
                    emitSharedFlow(response.message)
                    _logInUiState.update { newState->
                        newState.copy(authState = AuthState.NotLoggedIn)
                    }
                } else if (response is AuthState.LoggedIn) {
                    //save the user in sharedPreference if its logged in
                        getCurrentUserAndSaveIt()
                    _logInUiState.update { newState ->
                        newState.copy(authState = AuthState.LoggedIn)
                    }
                }
            }
        }
    }

    private suspend fun getCurrentUser(): String? {
        return getCurrentUserUseCase.getCurrentUser()
    }

    private suspend fun getCurrentUserAndSaveIt() {
        val currentUsername = getCurrentUser()
        saveSharedPrefUsernameUseCase.saveUsername(currentUsername)
    }


    private fun setEmail(email: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(emailValue = email)
            }
        }
    }

    private fun setPassword(password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(passwordValue = password)
            }
        }
    }

    private fun setShowPassword() {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

}

package com.example.donorbox.presentation.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.sharedpreferenceUsecase.SharedPreferenceUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object LoggedIn : AuthState
    data object NotLoggedIn : AuthState
    data object Loading : AuthState
    data class Error(val message: String) : AuthState
}

sealed interface LogInAction{
    data class SetEmail(val email: String): LogInAction
    data class OnPasswordChange(val passwordValue: String): LogInAction
    data class OnLogIn(val emailValue: String, val passwordValue: String): LogInAction
    data object SetShowPassword: LogInAction
}


class LogInViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {
    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()


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

    private fun emitMessage(message: String) {
        viewModelScope.launch {
            _eventMessage.send(message)
        }
    }


    private fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(authState = AuthState.Loading)
            }
            if (email.isEmpty() || password.isEmpty()) {
                emitMessage("Email and Password can't be empty")
                _logInUiState.update { newState ->
                    newState.copy(authState = AuthState.NotLoggedIn)
                }
            } else {
                val response = authenticationUseCase.logIn(email, password)
                if (response is AuthState.Error) {
                    emitMessage(response.message)
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
        return authenticationUseCase.getCurrentUser()
    }

    private suspend fun getCurrentUserAndSaveIt() {
        val currentUsername = getCurrentUser()
        sharedPreferenceUseCase.saveUsername(currentUsername)
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

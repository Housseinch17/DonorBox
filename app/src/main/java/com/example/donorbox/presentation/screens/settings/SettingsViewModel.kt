package com.example.donorbox.presentation.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface UpdatePassword {
    data class CurrentPassword(val currentPasswordValue: String): UpdatePassword
    data class NewPassword(val newPasswordValue: String): UpdatePassword
    data class ConfirmPassword(val confirmPasswordValue: String): UpdatePassword
}

sealed interface ShowPassword{
    data object CurrentPassword: ShowPassword
    data object NewPassword: ShowPassword
    data object ConfirmPassword: ShowPassword
}

sealed interface SettingsAction{
    data class UpdatePassword(val updatePassword: com.example.donorbox.presentation.screens.settings.UpdatePassword?): SettingsAction
    data class ShowPassword(val showPassword: com.example.donorbox.presentation.screens.settings.ShowPassword): SettingsAction
    data class OnPasswordChange(val username: String, val currentPassword: String, val newPassword: String, val confirmPassword: String): SettingsAction
}

class SettingsViewModel (
    private val authenticationUseCase: AuthenticationUseCase,
) : ViewModel() {
    private val _settingsUiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()


    init {
        Log.d("ViewModelInitialization", "SettingsViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "SettingsViewModel destroyed")
    }

    fun onActionSettings(settingsAction: SettingsAction){
        when(settingsAction){
            is SettingsAction.ShowPassword -> showPassword(
                showPassword = settingsAction.showPassword
            )
            is SettingsAction.UpdatePassword -> updatePassword(
                updatePassword = settingsAction.updatePassword
            )
            is SettingsAction.OnPasswordChange -> {
                viewModelScope.launch {
                    verifyPassword(
                        password = settingsAction.currentPassword,
                        onVerified = {
                            changePassword(
                                email = settingsAction.username,
                                newPassword = settingsAction.newPassword,
                                confirmNewPassword = settingsAction.confirmPassword
                            )
                        },
                        setError = { error->
                            emitValue(error)
                        }
                    )
                }
            }
        }
    }

    private fun updatePassword(updatePassword: UpdatePassword?){
        when(updatePassword){
            is UpdatePassword.ConfirmPassword -> {
                viewModelScope.launch {
                    _settingsUiState.update { newState ->
                        newState.copy(confirmNewPasswordValue = updatePassword.confirmPasswordValue)
                    }
                }
            }
            is UpdatePassword.CurrentPassword -> {
                viewModelScope.launch {
                    _settingsUiState.update { newState ->
                        newState.copy(currentPasswordValue = updatePassword.currentPasswordValue)
                    }
                }
            }
            is UpdatePassword.NewPassword -> {
                viewModelScope.launch {
                    _settingsUiState.update { newState ->
                        newState.copy(newPasswordValue = updatePassword.newPasswordValue)
                    }
                }
            }

            null -> {

            }
        }
    }
    
    fun showPassword(showPassword: ShowPassword?){
        when(showPassword){
            ShowPassword.ConfirmPassword -> {
                viewModelScope.launch {
                    _settingsUiState.update { newState ->
                        newState.copy(
                            confirmShowPassword = !newState.confirmShowPassword,
                        )
                    }
                }
            }
            ShowPassword.CurrentPassword -> {
                _settingsUiState.update{ newState ->
                    newState.copy(
                        currentShowPassword = !newState.currentShowPassword,
                    )
                }
            }
            ShowPassword.NewPassword -> {
                viewModelScope.launch {
                    _settingsUiState.update { newState ->
                        newState.copy(
                            newShowPassword = !newState.newShowPassword,
                        )
                    }
                }
            }

            null -> {
            }
        }
    }

    private fun emitValue(newText: String) {
        viewModelScope.launch {
            _eventMessage.send(newText)
        }
    }

    private suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit,
    ) {
        if(password.isEmpty()){
            emitValue("CurrentPassword is empty!")
        }else{
            authenticationUseCase.verifyPassword(password,onVerified,setError)
        }
    }

    private fun changePassword(email: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    showText = false,
                    confirmShowText = false,
                    isLoading = true,
                    passwordChangement = PasswordChangement.InitialState
                )
            }
            if (newPassword.length < 6) {
                _settingsUiState.update { newState ->
                    newState.copy(showText = true)
                }
            } else if (confirmNewPassword.length < 6) {
                _settingsUiState.update { newState ->
                    newState.copy(confirmShowText = true)
                }
            } else if (newPassword != confirmNewPassword) {
                emitValue("New password and confirm password should match")
                _settingsUiState.update { newState ->
                    newState.copy(passwordChangement = PasswordChangement.Error("Error"))
                }
            } else {
                delay(1000)
                val passwordChangement = authenticationUseCase.changePassword(email = email, newPassword = newPassword)
                _settingsUiState.update { newState ->
                    newState.copy(
                        currentPasswordValue = "",
                        newPasswordValue = "",
                        confirmNewPasswordValue = "",
                        showText = false,
                        confirmShowText = false,
                        passwordChangement = passwordChangement,
                    )
                }
                when (passwordChangement) {
                    is PasswordChangement.Error -> emitValue(passwordChangement.errorMessage)
                    is PasswordChangement.Success -> emitValue(passwordChangement.successMessage)
                    else -> {}
                }
            }
            _settingsUiState.update { newState ->
                newState.copy(isLoading = false)
            }
        }
    }

}


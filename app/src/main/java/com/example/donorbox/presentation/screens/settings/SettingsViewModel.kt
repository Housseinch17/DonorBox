package com.example.donorbox.presentation.screens.settings

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.ChangePasswordUseCase
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {
    private val _settingsUiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private val _emitValue: MutableSharedFlow<String> = MutableSharedFlow()
    val emitValue: SharedFlow<String> = _emitValue.asSharedFlow()

    init {
        Log.d("ViewModelInitialization", "SettingsViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "SettingsViewModel destroyed")
    }


    fun newPasswordValueChange(newPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(newPasswordValue = newPassword)
            }
        }
    }

    fun confirmNewPasswordValueChange(confirmNewPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(confirmNewPasswordValue = confirmNewPassword)
            }
        }
    }

    fun setShowPassword() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    fun setConfirmShowPassword() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    confirmShowPassword = !newState.confirmShowPassword,
                )
            }
        }
    }

    private fun emitValue(newText: String) {
        viewModelScope.launch {
            _emitValue.emit(newText)
        }
    }

    fun changePassword(email: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    showText = false,
                    confirmShowText = false,
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
                val passwordChangement =
                    changePasswordUseCase.changePassword(email = email, newPassword = newPassword)
                _settingsUiState.update { newState ->
                    newState.copy(
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
        }
    }

    fun getIconVisibility(showPassword: Boolean): ImageVector {
        return if (showPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }
}


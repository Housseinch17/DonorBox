package com.example.donorbox.presentation.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val firebaseReadDataUseCase: FirebaseReadDataUseCase
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    init {
        Log.d("ViewModelInitialization", "ProfileViewModel created")
        viewModelScope.launch {
            getCurrentUser()
            getFullName()
            setLoader()
        }
    }

    private suspend fun getCurrentUser() {
        val username = authenticationUseCase.getCurrentUser() ?: ""
        val maskedEmail = if(username.isNotEmpty()) maskEmail(username) else username
        Log.d("MyTag","maskedEmail: $maskedEmail")
        _profileUiState.update { newState ->
            newState.copy(username = maskedEmail)
        }
    }

    private suspend fun getFullName() {
        val name = firebaseReadDataUseCase.readFullNameByUsername()
        _profileUiState.update { newState ->
            newState.copy(name = name)
        }
    }

    private fun setLoader() {
        _profileUiState.update { newState ->
            newState.copy(isLoading = false)
        }
    }

    private fun maskEmail(email: String): String {
        val parts = email.split("@")
        val localPart = parts[0]
        val halfLength = localPart.length / 2  // Take half of the email
        val maskedLocalPart = localPart.take(halfLength) + "*".repeat(localPart.length - halfLength)
        return maskedLocalPart + "@" + parts[1]
    }


}
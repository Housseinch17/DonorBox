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
        _profileUiState.update { newState ->
            newState.copy(username = username)
        }
    }

    private suspend fun getFullName() {
        val name = firebaseReadDataUseCase.readFullNameByUsername()
        _profileUiState.update { newState ->
            newState.copy(name = name)
        }
    }

    private fun setLoader(){
        _profileUiState.update { newState->
            newState.copy(isLoading = false)
        }
    }
}
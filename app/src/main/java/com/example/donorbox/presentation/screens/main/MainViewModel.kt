package com.example.donorbox.presentation.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.donorbox.presentation.navigation.NavigationScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : ViewModel() {
    private val _mainUiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    init {
        checkSharedPreferUsername()
            Log.d("ViewModelInitialization","MainViewModel created")
    }

    private suspend fun getCurrentUserName(): String? {
        return getSharedPrefUsernameUseCase.getUsername()
    }

    private fun checkSharedPreferUsername() {
        viewModelScope.launch {
            //read username from sharedPreference
            val currentUsername = getCurrentUserName()
            if (currentUsername == null) {
                _mainUiState.update { newState->
                    newState.copy(currentScreen = NavigationScreens.RegisterGraph)
                }
            } else {
                _mainUiState.update { newState->
                    newState.copy(currentScreen = NavigationScreens.DonorBoxGraph)
                }
            }
        }
    }
}
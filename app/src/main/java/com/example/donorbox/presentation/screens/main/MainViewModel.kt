package com.example.donorbox.presentation.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.donorbox.presentation.navigation.NavigationScreens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : ViewModel() {
    private val _status: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState())
    val status: StateFlow<MainUiState> = _status.asStateFlow()

    init {
        checkSharedPreferUsername()
            Log.d("ViewModelInitialization","MainViewModel created")
    }

    private suspend fun getCurrentUserName(): String? {
        return getSharedPrefUsernameUseCase.getUsername()
    }

    private fun showBottomBar(){
        Log.d("BottomBarLoading", "Entered")
        viewModelScope.launch {
            _status.update { newState->
                newState.copy(isLoading = false)
            }
        }
    }

    private fun checkSharedPreferUsername() {
        viewModelScope.launch {
            _status.update { it.copy(isLoading = true) }
            //read username from sharedPreference
            val currentUsername = getCurrentUserName()
            if (currentUsername == null) {
                _status.update { newState->
                    newState.copy(currentScreen = NavigationScreens.Register)
                }
            } else {
                _status.update { newState->
                    newState.copy(currentScreen = NavigationScreens.DonorBox)
                }
            }

            // delay used to wait HomePage to load instead of loading bottomBar before homepage load
            delay(2000)
            showBottomBar()
        }
    }
}
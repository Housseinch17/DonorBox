package com.example.donorbox.presentation.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.donorbox.presentation.navigation.NavigationScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : ViewModel() {
    private val _status: MutableStateFlow<NavigationScreens> =
        MutableStateFlow(NavigationScreens.Loading)
    val status: StateFlow<NavigationScreens> = _status.asStateFlow()

    init {
        checkSharedPreferUsername()
            Log.d("ViewModelInitialization","Main created")
    }

    private suspend fun getCurrentUserName(): String? {
        return getSharedPrefUsernameUseCase.getUsername()
    }

    private fun checkSharedPreferUsername() {
        viewModelScope.launch {
            //read username from sharedPreference
            val currentUsername = getCurrentUserName()
            if (currentUsername == null) {
                _status.value = NavigationScreens.Register
            } else {
                _status.value = NavigationScreens.DonorBox
            }
        }
    }
}
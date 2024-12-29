package com.example.donorbox.presentation.screens.mydonations

import androidx.lifecycle.ViewModel
import com.example.donorbox.domain.useCase.localDataBaseUseCase.GetAllDonationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyDonationsViewModel(
    private val getAllDonationsUseCase: GetAllDonationsUseCase
): ViewModel() {
    private val _myDonationsUiState: MutableStateFlow<MyDonationsUiState> = MutableStateFlow(MyDonationsUiState())
    val myDonationsUiState: StateFlow<MyDonationsUiState> = _myDonationsUiState.asStateFlow()


}
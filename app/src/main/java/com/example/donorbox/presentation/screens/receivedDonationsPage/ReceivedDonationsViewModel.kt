package com.example.donorbox.presentation.screens.receivedDonationsPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ReceivedDonationsAction{
    data object LoadNewOrders: ReceivedDonationsAction
}

class ReceivedDonationsViewModel(
    private val firebaseReadDataUseCase: FirebaseReadDataUseCase
) : ViewModel() {
    private val _receivedDonationsUiState: MutableStateFlow<ReceivedDonationsUiState> =
        MutableStateFlow(
            ReceivedDonationsUiState()
        )
    val receivedDonationsUiState = _receivedDonationsUiState.asStateFlow()

    init {
        Log.d("ViewModelInitialization", "ReceivedDonationsViewModel created")
        viewModelScope.launch {
            readAllDonations()
        }
    }

    fun onActionReceivedDonations(receivedDonationsAction: ReceivedDonationsAction){
        when(receivedDonationsAction){
            ReceivedDonationsAction.LoadNewOrders -> loadNewOrders()
        }
    }


    private suspend fun readAllDonations() {
        _receivedDonationsUiState.update { newState ->
            newState.copy(isLoading = true)
        }
        val receivedDonationsList = firebaseReadDataUseCase.readAllDonations()
        _receivedDonationsUiState.update { newState ->
            newState.copy(
                receivedDonationsList = receivedDonationsList,
                isLoading = false
            )
        }
    }

    private fun loadNewOrders() {
        viewModelScope.launch {
            _receivedDonationsUiState.update { newState ->
                newState.copy(isRefreshing = true)
            }
            resetAllOrders()
            delay(1000)
            readAllDonations()
            _receivedDonationsUiState.update { newState ->
                newState.copy(isRefreshing = false)
            }
        }
    }

    //here isLoading is set true because in loadNewOrders() its showing No orders yet in MyOrdersPage
    //because of the condition which set list empty and refreshing false
    private fun resetAllOrders() {
        viewModelScope.launch {
            _receivedDonationsUiState.value = ReceivedDonationsUiState().copy(isRefreshing = true)
        }
    }

}
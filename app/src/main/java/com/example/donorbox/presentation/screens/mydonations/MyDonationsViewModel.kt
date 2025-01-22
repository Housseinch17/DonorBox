package com.example.donorbox.presentation.screens.mydonations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.localDataBaseUseCase.LocalDataBaseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MyDonationAction{
    data object OnRefresh: MyDonationAction
}


class MyDonationsViewModel(
    private val localDataBaseUseCase: LocalDataBaseUseCase
): ViewModel() {
    private val _myDonationsUiState: MutableStateFlow<MyDonationsUiState> =
        MutableStateFlow(MyDonationsUiState())
    val myDonationsUiState: StateFlow<MyDonationsUiState> = _myDonationsUiState.asStateFlow()


    init {
        viewModelScope.launch {
            Log.d("ViewModelInitialization","MyDonationsViewModel created")
            getAllDonations()
        }
    }

    fun onActionMyDonations(myDonationAction: MyDonationAction){
        when(myDonationAction){
            MyDonationAction.OnRefresh -> loadNewOrders()
        }
    }

    private suspend fun getAllDonations() {
        _myDonationsUiState.update { newState ->
            newState.copy(isLoading = true)
        }
        val getAllDonations = localDataBaseUseCase.getAllDonations()
        _myDonationsUiState.update { newState ->
            newState.copy(
                list = getAllDonations,
                isLoading = false
            )
        }
        Log.d("MyTag", "getAllDonations() finished")
    }

   private fun loadNewOrders() {
        viewModelScope.launch {
            _myDonationsUiState.update { newState ->
                newState.copy(isRefreshing = true)
            }
            Log.d("MyTag", "refreshing true")
            resetAllOrders()
            delay(1000)
            getAllDonations()
            _myDonationsUiState.update { newState ->
                newState.copy(isRefreshing = false)
            }
            Log.d("MyTag", "loadNewOrders() finished")
        }
    }

    //here isLoading is set true because in loadNewOrders() its showing No orders yet in MyOrdersPage
    //because of the condition which set list empty and refreshing false
    private fun resetAllOrders() {
        viewModelScope.launch {
            _myDonationsUiState.value = MyDonationsUiState().copy(isRefreshing = true)
        }
        Log.d("MyTag", "resetAllOrders() finished")

    }
}
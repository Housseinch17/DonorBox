package com.example.donorbox.presentation.screens.mydonations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.localDataBaseUseCase.LocalDataBaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
            MyDonationAction.OnRefresh -> {

            }
        }
    }

    private suspend fun getAllDonations() {
        _myDonationsUiState.update { newState ->
            newState.copy(isLoading = true)
        }
        localDataBaseUseCase.getAllDonations().collectLatest { donations->
            _myDonationsUiState.update { newState ->
                newState.copy(
                    list = donations,
                    isLoading = false
                )
            }
        }
        Log.d("MyTag", "getAllDonations() finished")
    }
}
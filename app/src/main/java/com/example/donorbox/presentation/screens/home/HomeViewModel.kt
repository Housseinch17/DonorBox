package com.example.donorbox.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseReadDataUseCase.FirebaseReadReceiversUseCase
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val firebaseReadReceiversUseCase: FirebaseReadReceiversUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            readValues()
        }
    }

    private fun emitFlow(message: String){
        viewModelScope.launch {
            _sharedFlow.emit(message)
        }
    }

    private suspend fun readValues(){

        val response = firebaseReadReceiversUseCase.readReceivers()
        Log.d("MyTag","$response")
        if(response is ReceiversResponse.Error){
            emitFlow(response.message)
        }
        _uiState.update { newState->
            newState.copy(receiversResponse = response)
        }
    }
}
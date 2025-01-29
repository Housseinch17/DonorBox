package com.example.donorbox.presentation.screens.contactUs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.data.model.ContactUs
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDataUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ContactUsAction{
    data class UpdateName(val newName: String): ContactUsAction
    data class UpdateTitle(val newTitle: String): ContactUsAction
    data class UpdateMessage(val newMessage: String): ContactUsAction
    data class ContactUs(val contactUs: com.example.donorbox.data.model.ContactUs): ContactUsAction
}

class ContactUsViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val firebaseWriteDataUseCase: FirebaseWriteDataUseCase
): ViewModel() {
    private val _contactUsUiState: MutableStateFlow<ContactUsUiState> = MutableStateFlow(
        ContactUsUiState()
    )
    val contactUsUiState: StateFlow<ContactUsUiState> = _contactUsUiState.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()

    fun onActionContactUs(contactUsAction: ContactUsAction){
        when(contactUsAction){
            is ContactUsAction.ContactUs -> {
                viewModelScope.launch {
                    contactUs(contactUsAction.contactUs)
                }
            }
            is ContactUsAction.UpdateMessage -> updateMessage(contactUsAction.newMessage)
            is ContactUsAction.UpdateName -> updateName(contactUsAction.newName)
            is ContactUsAction.UpdateTitle -> updateTitle(contactUsAction.newTitle)
        }
    }

    private suspend fun contactUs(contactUs: ContactUs){
        _contactUsUiState.update { newState->
            newState.copy(isLoading = true)
        }
        val username = authenticationUseCase.getCurrentUser() ?: ""
        if(contactUs.name.isNotEmpty()){
            if(contactUs.message.isEmpty() || contactUs.title.isEmpty()){
                emitValue("Title or Message Empty")
            }else{
               val contactUsTask =  firebaseWriteDataUseCase.contactUs(contactUs, username)
                emitValue(contactUsTask)
                _contactUsUiState.update { newState->
                    newState.copy(title = "", message = "", name = "")
                }
            }
        }else{
            emitValue("Name is empty!")
        }
        _contactUsUiState.update { newState->
            newState.copy(isLoading = false)
        }
    }

    private fun emitValue(newText: String) {
        viewModelScope.launch {
            _eventMessage.send(newText)
        }
    }

    private fun updateName(newName: String){
        viewModelScope.launch {
            _contactUsUiState.update { newState->
                newState.copy(name = newName)
            }
        }
    }

    private fun updateTitle(newTitle: String){
        viewModelScope.launch {
            _contactUsUiState.update { newState->
                newState.copy(title = newTitle)
            }
        }
    }

    private fun updateMessage(newMessage: String){
        viewModelScope.launch {
            _contactUsUiState.update { newState->
                newState.copy(message = newMessage)
            }
        }
    }

}
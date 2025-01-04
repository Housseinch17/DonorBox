package com.example.donorbox.presentation.screens.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.data.model.notificationMessage.Message
import com.example.donorbox.data.model.notificationMessage.Notification
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.VerifyPasswordUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadFullNameUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadReceiversUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDonationsUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.SendNotificationToTokenUseCase
import com.example.donorbox.domain.useCase.localDataBaseUseCase.SaveDonationsUseCase
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val firebaseReadReceiversUseCase: FirebaseReadReceiversUseCase,
    private val saveDonationsUseCase: SaveDonationsUseCase,
    private val verifyPasswordUseCase: VerifyPasswordUseCase,
    private val sendNotificationToTokenUseCase: SendNotificationToTokenUseCase,
    private val firebaseReadFullNameUseCase: FirebaseReadFullNameUseCase,
    private val firebaseWriteDonationsUseCase: FirebaseWriteDonationsUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            Log.d("ViewModelInitialization", "HomeViewModel created")
            readValues()

        }
    }

    private suspend fun readFullName() {
        val fullName = firebaseReadFullNameUseCase.readFullNameByUsername()
        _uiState.update { newState ->
            newState.copy(
                senderFullName = fullName
            )
        }
    }

    private suspend fun sendNotification(notificationMessage: NotificationMessage) {
        sendNotificationToTokenUseCase.sendNotificationToToken(
            notificationMessage = notificationMessage
        )
    }

    fun updateCurrentTokenAndUsername(token: String, username: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(receiverToken = token, receiverUsername = username)
            }
        }
    }

    private fun emitFlow(message: String) {
        viewModelScope.launch {
            _sharedFlow.emit(message)
        }
    }

    suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit,
    ) {
        if (password.isEmpty()) {
            updateLoader(false)
            emitFlow("Password is Empty")
        } else {
            verifyPasswordUseCase.verifyPassword(password, onVerified, setError)
        }
    }

    fun newPasswordValueChange(newPassword: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(newPasswordValue = newPassword)
            }
        }
    }

    fun setShowPassword() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    fun showDialog() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(dialogVisibility = true)
            }
        }
    }

    private fun updateLoader(loaderVisibility: Boolean) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(isLoading = loaderVisibility)
            }
        }
    }

    fun hideDialog() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(dialogVisibility = false)
            }
        }
    }

    fun updateMoneyToDonate(moneyToDonate: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(moneyToDonate = moneyToDonate)
            }
        }
    }

    suspend fun sendMoney(moneyToDonate: String, donations: MyDonations) {
        val homeUiState = _uiState.value
        _uiState.update { newState ->
            newState.copy(showText = false)
        }
        if (moneyToDonate.isEmpty()) {
            _uiState.update { newState ->
                newState.copy(showText = true)
            }
        } else {
            try {
                updateLoader(true)

                //save donation first
                saveDonationsUseCase.saveDonations(donations)

                //read full name of sender
                readFullName()

                //send notification
                sendNotification(
                    notificationMessage = NotificationMessage(
                        message = Message(
                            token = homeUiState.receiverToken,
                            notification = Notification(
                                title = "Received Money",
                                body = "You have received $moneyToDonate$ from ${homeUiState.senderFullName.currentName} ${homeUiState.senderFullName.currentFamily}"
                            )
                        )
                    )
                )

                //post to receiver firebase
                firebaseWriteDonationsUseCase.writeDonationsIntoFirebase(
                    homeUiState.receiverUsername,
                    "You have received $moneyToDonate\$ from ${homeUiState.senderFullName.currentName} ${homeUiState.senderFullName.currentFamily}"
                )

                updateMoneyToDonate("")
                newPasswordValueChange("")
                updateLoader(false)

                emitFlow("You're donations are succeed!")
                hideDialog()
            } catch (e: Exception) {
                emitFlow(e.message.toString())
            }
        }
        updateLoader(false)

    }

    private suspend fun readValues() {
        val response = firebaseReadReceiversUseCase.readReceivers()
        Log.d("MyTag", "readValues() $response")
        if (response is ReceiversResponse.Error) {
            emitFlow(response.message)
        }

        //take the first receiver as the modalBottomSheetReceiver
        if (response is ReceiversResponse.Success) {
            _uiState.update { newState ->
                newState.copy(
                    modalBottomSheetReceiver = newState.modalBottomSheetReceiver.copy(
                        modalBottomSheetReceiver = response.receivers.first()
                    )
                )
            }
        }

        _uiState.update { newState ->
            newState.copy(receiversResponse = response)
        }
    }

    fun showBottomSheetReceiver() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(
                    modalBottomSheetReceiver = newState.modalBottomSheetReceiver.copy(
                        showBottomSheet = true
                    )
                )
            }
        }
    }

    fun hideBottomSheetReceiver() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(
                    modalBottomSheetReceiver = newState.modalBottomSheetReceiver.copy(
                        showBottomSheet = false
                    )
                )
            }
        }
    }

    //update ModalBottomSheetReceiver
    fun updateModalBottomSheetReceiver(modalBottomSheetReceiver: Receiver) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(
                    modalBottomSheetReceiver = newState.modalBottomSheetReceiver.copy(
                        modalBottomSheetReceiver = modalBottomSheetReceiver
                    )
                )
            }
        }
    }

    fun getIconVisibility(showPassword: Boolean): ImageVector {
        return if (showPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }

}
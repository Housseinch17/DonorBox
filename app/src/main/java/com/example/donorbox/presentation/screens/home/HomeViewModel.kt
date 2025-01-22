package com.example.donorbox.presentation.screens.home

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.MyApplication
import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.data.model.notificationMessage.Message
import com.example.donorbox.data.model.notificationMessage.Notification
import com.example.donorbox.data.model.notificationMessage.NotificationMessage
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseReadDataUseCase.FirebaseReadDataUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDataUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.notificationUseCase.NotificationUseCase
import com.example.donorbox.domain.useCase.localDataBaseUseCase.LocalDataBaseUseCase
import com.example.donorbox.presentation.util.callPhoneDirectly
import com.example.donorbox.presentation.util.openApp
import com.example.donorbox.presentation.util.openGoogleMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ReceiversResponse {
    data class Success(val receivers: List<Receiver>) : ReceiversResponse
    data object IsLoading : ReceiversResponse
    data class Error(val message: String) : ReceiversResponse
}

sealed interface HomeAction {
    data class OnReceiverClick(val receiver: Receiver) : HomeAction
    data class OnOpenGoogleMap(val context: Context, val latitude: Double, val longitude: Double) :
        HomeAction

    data class OnSendButton(val receiverToken: String, val receiverUsername: String) : HomeAction
    data class SendMoney(val moneyToDonate: String, val password: String) : HomeAction
    data class OnMoneyUpdate(val moneyValue: String) : HomeAction
    data class NewPasswordValueChange(val newPasswordValueChange: String) : HomeAction
    data class OnCall(val context: Context, val phoneNumber: String) : HomeAction
    data class OnOpenOmtApp(val context: Context) : HomeAction
    data class OnOpenWhishApp(val context: Context) : HomeAction
    data object HideBottomSheetReceiver : HomeAction
    data object HideDialog : HomeAction
    data object OnIconClick : HomeAction
}


class HomeViewModel(
    private val application: Application,
    private val firebaseWriteDataUseCase: FirebaseWriteDataUseCase,
    private val firebaseReadDataUseCase: FirebaseReadDataUseCase,
    private val localDataBaseUseCase: LocalDataBaseUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val notificationUseCase: NotificationUseCase
) : AndroidViewModel(application) {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()

    init {
        viewModelScope.launch {
            Log.d("ViewModelInitialization", "HomeViewModel created")
            readValues()

        }
    }


    fun onActionHome(homeAction: HomeAction) {
        when (homeAction) {
            HomeAction.HideBottomSheetReceiver -> hideBottomSheetReceiver()
            HomeAction.HideDialog -> hideDialog()
            is HomeAction.NewPasswordValueChange -> newPasswordValueChange(
                newPassword = homeAction.newPasswordValueChange
            )

            is HomeAction.OnCall -> {
                homeAction.context.callPhoneDirectly(homeAction.phoneNumber) {
                    Toast.makeText(homeAction.context, "Permission not granted!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            HomeAction.OnIconClick -> setShowPassword()
            is HomeAction.OnMoneyUpdate -> updateMoneyToDonate(
                moneyToDonate = homeAction.moneyValue
            )

            is HomeAction.OnOpenGoogleMap -> {
                homeAction.context.openGoogleMap(
                    latitude = homeAction.latitude,
                    longitude = homeAction.longitude
                )
            }

            is HomeAction.OnOpenOmtApp -> {
                homeAction.context.openApp(packageName = "com.tedmob.omt")
            }

            is HomeAction.OnOpenWhishApp -> {
                homeAction.context.openApp(packageName = "money.whish.android")
            }

            is HomeAction.OnReceiverClick -> {
                updateModalBottomSheetReceiver(modalBottomSheetReceiver = homeAction.receiver)
                showBottomSheetReceiver()
            }

            is HomeAction.OnSendButton -> {
                showDialog()
                updateCurrentTokenAndUsername(
                    token = homeAction.receiverToken,
                    username = homeAction.receiverUsername
                )
            }

            is HomeAction.SendMoney -> {
                updateLoader(true)
                viewModelScope.launch {
                    verifyPassword(
                        moneyToDonate = homeAction.moneyToDonate,
                        password = homeAction.password,
                        onVerified = {
                            viewModelScope.launch {
                                sendMoney(
                                    moneyToDonate = homeAction.moneyToDonate,
                                    donations = MyDonations(
                                        myDonations = "Donated ${homeAction.moneyToDonate}$  to: ${_uiState.value.modalBottomSheetReceiver.modalBottomSheetReceiver.name}"
                                    )
                                )
                            }
                        },
                        setError = { error ->
                            emitFlow(error)
                            updateLoader(false)
                        }
                    )
                }
            }
        }
    }

    private suspend fun readFullName() {
        Log.d("MyTag", "readFullName")
        val name = firebaseReadDataUseCase.readFullNameByUsername()
        Log.d("MyTag", "read $name")
        _uiState.update { newState ->
            newState.copy(
                senderName = name
            )
        }
        Log.d("MyTag", _uiState.value.senderName)
    }

    private suspend fun sendNotification(notificationMessage: NotificationMessage) {
        Log.d("MyTag", "sendNotification")
        notificationUseCase.sendNotificationToToken(
            notificationMessage = notificationMessage
        )
    }

    private fun updateCurrentTokenAndUsername(token: String, username: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(receiverToken = token, receiverUsername = username)
            }
        }
    }

    private fun emitFlow(message: String) {
        viewModelScope.launch {
            _eventMessage.send(message)
        }
    }

    private suspend fun verifyPassword(
        moneyToDonate: String,
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit,
    ) {
        if (password.isEmpty()) {
            updateLoader(false)
            emitFlow("Password is Empty")
        } else if (moneyToDonate.isEmpty()) {
            _uiState.update { newState ->
                newState.copy(showText = true)
            }
            updateLoader(false)
        } else {
            authenticationUseCase.verifyPassword(password, onVerified, setError)
        }
    }

    private fun newPasswordValueChange(newPassword: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(newPasswordValue = newPassword)
            }
        }
    }

    private fun setShowPassword() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    private fun showDialog() {
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

    private fun hideDialog() {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(dialogVisibility = false)
            }
        }
    }

    private fun updateMoneyToDonate(moneyToDonate: String) {
        viewModelScope.launch {
            _uiState.update { newState ->
                newState.copy(moneyToDonate = moneyToDonate)
            }
        }
    }

    private fun sendMoney(moneyToDonate: String, donations: MyDonations) {
        _uiState.update { newState ->
            newState.copy(showText = false)
        }
        if (moneyToDonate.isEmpty()) {
            _uiState.update { newState ->
                newState.copy(showText = true)
            }
            updateLoader(false)
        } else {
            //getting application scope from MyApplication class
            //it will only cancel if any child failed/cancelled or the whole app destroyed
            val appScope = (application as MyApplication).applicationScope

            appScope.launch {
                try {
                    //read full name of sender
                    readFullName()

                    val homeUiState = _uiState.value

                    Log.d("MyTag", "HomeUiState ${homeUiState.senderName}")
                    //send notification
                    sendNotification(
                        notificationMessage = NotificationMessage(
                            message = Message(
                                token = homeUiState.receiverToken,
                                notification = Notification(
                                    title = "Received Money",
                                    body = "You have received $moneyToDonate$ from ${homeUiState.senderName}"
                                )
                            )
                        )
                    )

                    //post to receiver firebase
                    firebaseWriteDataUseCase.writeDonationsIntoFirebase(
                        homeUiState.receiverUsername,
                        "You have received $moneyToDonate\$ from ${homeUiState.senderName}"
                    )
                    //save donation
                    localDataBaseUseCase.saveDonations(donations)

                    _uiState.update { newState ->
                        newState.copy(
                            moneyToDonate = "",
                            newPasswordValue = "",
                            isLoading = false
                        )
                    }

                    emitFlow("You're donations are succeed!")
                    hideDialog()
                } catch (e: Exception) {
                    Log.d("MyTag", "sendMoney() error: ${e.message}")
                    emitFlow(e.message.toString())
                    updateLoader(false)
                }
            }
            updateLoader(false)
        }
    }

    private suspend fun readValues() {
        val response = firebaseReadDataUseCase.readReceivers()
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

    private fun showBottomSheetReceiver() {
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

    private fun hideBottomSheetReceiver() {
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
    private fun updateModalBottomSheetReceiver(modalBottomSheetReceiver: Receiver) {
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

}
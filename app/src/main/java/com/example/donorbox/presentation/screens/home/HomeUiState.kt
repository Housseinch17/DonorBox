package com.example.donorbox.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.donorbox.data.model.Receiver


@Stable
data class HomeUiState(
    val receiversResponse: ReceiversResponse = ReceiversResponse.IsLoading,
    val modalBottomSheetReceiver: ModalBottomSheetReceiver = ModalBottomSheetReceiver(),
    val dialogVisibility: Boolean = false,
    val moneyToDonate: String = "",
    val isLoading: Boolean = false,
    val showText: Boolean = false,
    val newPasswordValue: String = "",
    val showPassword: Boolean = false,
    val receiverToken: String = "",
    val receiverUsername: String = "",
    val senderName: String = "",

)

@Immutable
data class ModalBottomSheetReceiver(
    val modalBottomSheetReceiver: Receiver = Receiver(),
    val showBottomSheet: Boolean = true
)
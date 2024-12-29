package com.example.donorbox.presentation.screens.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse


@Stable
data class HomeUiState(
    val receiversResponse: ReceiversResponse = ReceiversResponse.IsLoading,
    val modalBottomSheetReceiver: ModalBottomSheetReceiver = ModalBottomSheetReceiver(),
    val dialogVisibility: Boolean = false,
    val moneyToDonate: String = "",
    val isLoading: Boolean = false
)

@Immutable
data class ModalBottomSheetReceiver(
    val modalBottomSheetReceiver: Receiver = Receiver(),
    val showBottomSheet: Boolean = true
)
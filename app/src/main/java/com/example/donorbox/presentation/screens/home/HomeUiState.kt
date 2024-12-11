package com.example.donorbox.presentation.screens.home

import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

data class HomeUiState(
    val receiversResponse: ReceiversResponse = ReceiversResponse.IsLoading,
)

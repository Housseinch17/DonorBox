package com.example.donorbox.presentation.sealedInterfaces

sealed interface ModalBottomSheetImplicitIntent {
    data class Call(val phoneNumber: Int): ModalBottomSheetImplicitIntent
    data class OpenGoogleMap(val geo: String): ModalBottomSheetImplicitIntent
    data class  OpenOmt(val omtPackage: String): ModalBottomSheetImplicitIntent
    data class OpenWhish(val whishPackage: String): ModalBottomSheetImplicitIntent
}


package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.model.MyDonations

interface LocalDataBaseUseCase {
    suspend fun getAllDonations(): List<MyDonations>
    suspend fun saveDonations(donations: MyDonations)
}
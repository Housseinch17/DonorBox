package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.model.MyDonations
import kotlinx.coroutines.flow.Flow

interface LocalDataBaseUseCase {
    fun getAllDonations(): Flow<List<MyDonations>>
    suspend fun saveDonations(donations: MyDonations)
}
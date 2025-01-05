package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository

class SaveDonationsUseCase(private val localDataBaseRepository: LocalDataBaseRepository) {
    suspend fun saveDonations(donations: MyDonations){
        localDataBaseRepository.saveDonations(donations)
    }
}
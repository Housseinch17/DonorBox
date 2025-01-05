package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository

class GetAllDonationsUseCase(private val localDataBaseRepository: LocalDataBaseRepository) {
    suspend fun getAllDonations(): List<MyDonations> {
        return localDataBaseRepository.getAllDonations()
    }
}
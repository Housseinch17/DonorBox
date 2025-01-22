package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository

class LocalDataBaseUseCaseImpl(
    private val localDataBaseRepository: LocalDataBaseRepository
): LocalDataBaseUseCase {
    override suspend fun getAllDonations(): List<MyDonations> {
        return localDataBaseRepository.getAllDonations()
    }
    override suspend fun saveDonations(donations: MyDonations) {
        localDataBaseRepository.saveDonations(donations)
    }
}
package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.dataSource.localdatabase.LocalDataBaseRepositoryImpl
import com.example.donorbox.data.model.MyDonations

class GetAllDonationsUseCase(private val localDataBaseRepositoryImpl: LocalDataBaseRepositoryImpl) {
    suspend fun getAllDonations(): List<MyDonations> {
        return localDataBaseRepositoryImpl.getAllDonations()
    }
}
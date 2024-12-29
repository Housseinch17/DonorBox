package com.example.donorbox.domain.useCase.localDataBaseUseCase

import com.example.donorbox.data.dataSource.localdatabase.LocalDataBaseRepositoryImpl
import com.example.donorbox.data.model.MyDonations

class SaveDonationsUseCase(private val dataBaseRepositoryImpl: LocalDataBaseRepositoryImpl) {
    suspend fun saveDonations(donations: MyDonations){
        dataBaseRepositoryImpl.saveDonations(donations)
    }
}
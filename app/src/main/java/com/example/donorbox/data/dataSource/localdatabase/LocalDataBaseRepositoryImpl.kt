package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository

class LocalDataBaseRepositoryImpl(
    private val localDataBaseDataSourceImpl: LocalDataBaseDataSourceImpl
): LocalDataBaseRepository {
    override suspend fun saveDonations(donations: MyDonations) {
        localDataBaseDataSourceImpl.saveDonations(donations)
    }

    override suspend fun getAllDonations(): List<MyDonations> {
        return localDataBaseDataSourceImpl.getAllDonations()
    }
}
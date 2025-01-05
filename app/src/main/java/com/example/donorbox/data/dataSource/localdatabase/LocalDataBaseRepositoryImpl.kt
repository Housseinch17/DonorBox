package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository

class LocalDataBaseRepositoryImpl(
    private val localDataBaseDataSource: LocalDataBaseDataSource,
): LocalDataBaseRepository {
    override suspend fun saveDonations(donations: MyDonations) {
        localDataBaseDataSource.saveDonations(donations)
    }

    override suspend fun getAllDonations(): List<MyDonations> {
        return localDataBaseDataSource.getAllDonations()
    }
}
package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.domain.repository.LocalDataBaseRepository
import kotlinx.coroutines.flow.Flow

class LocalDataBaseRepositoryImpl(
    private val localDataBaseDataSource: LocalDataBaseDataSource,
): LocalDataBaseRepository {
    override suspend fun saveDonations(donations: MyDonations) {
        localDataBaseDataSource.saveDonations(donations)
    }

    override fun getAllDonations(): Flow<List<MyDonations>> {
        return localDataBaseDataSource.getAllDonations()
    }
}
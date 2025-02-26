package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations
import kotlinx.coroutines.flow.Flow

interface LocalDataBaseDataSource {
    suspend fun saveDonations(donations: MyDonations)
    fun getAllDonations(): Flow<List<MyDonations>>
}
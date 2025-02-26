package com.example.donorbox.domain.repository

import com.example.donorbox.data.model.MyDonations
import kotlinx.coroutines.flow.Flow

interface LocalDataBaseRepository {
    suspend fun saveDonations(donations: MyDonations)
    fun getAllDonations(): Flow<List<MyDonations>>
}
package com.example.donorbox.domain.repository

import com.example.donorbox.data.model.MyDonations

interface LocalDataBaseRepository {
    suspend fun saveDonations(donations: MyDonations)
    suspend fun getAllDonations(): List<MyDonations>
}
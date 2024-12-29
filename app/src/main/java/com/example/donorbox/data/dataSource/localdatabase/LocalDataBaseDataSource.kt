package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations

interface LocalDataBaseDataSource {
    suspend fun saveDonations(donations: MyDonations)
    suspend fun getAllDonations(): List<MyDonations>
}
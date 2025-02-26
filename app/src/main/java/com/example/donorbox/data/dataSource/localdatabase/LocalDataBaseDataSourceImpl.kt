package com.example.donorbox.data.dataSource.localdatabase

import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.data.roomdb.MyDonationsDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataBaseDataSourceImpl(
    private val myDonationsDAO: MyDonationsDAO,
    private val coroutineDispatcher: CoroutineDispatcher,
) : LocalDataBaseDataSource {
    override suspend fun saveDonations(donations: MyDonations) = withContext(coroutineDispatcher) {
        myDonationsDAO.saveDonations(donations)
    }

    override fun getAllDonations(): Flow<List<MyDonations>> {
        return myDonationsDAO.getAllDonations()
    }
}
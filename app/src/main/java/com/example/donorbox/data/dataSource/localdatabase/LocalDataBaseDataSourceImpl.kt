package com.example.donorbox.data.dataSource.localdatabase

import android.util.Log
import com.example.donorbox.data.model.MyDonations
import com.example.donorbox.data.roomdb.MyDonationsDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDataBaseDataSourceImpl(
    private val myDonationsDAO: MyDonationsDAO,
    private val coroutineDispatcher: CoroutineDispatcher,
): LocalDataBaseDataSource {
    override suspend fun saveDonations(donations: MyDonations) = withContext(coroutineDispatcher){
        myDonationsDAO.saveDonations(donations)
    }

    override suspend fun getAllDonations(): List<MyDonations> = withContext(coroutineDispatcher){
        val myDonationsList = try {
            myDonationsDAO.getAllDonations()
        } catch (e: Exception) {
            emptyList()
        }
        for(i in myDonationsList.indices){
            Log.d("ShowResult", myDonationsDAO.getAllDonations()[i].toString())
        }
        return@withContext myDonationsList
    }
}
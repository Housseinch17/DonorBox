package com.example.donorbox.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.donorbox.data.model.MyDonations
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDonationsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDonations(myDonations: MyDonations)

    //can use flow for live updates but prefer to implement refresh
    @Query("SELECT * FROM myDonations_table")
    fun getAllDonations(): Flow<List<MyDonations>>
}
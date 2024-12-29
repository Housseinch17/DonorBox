package com.example.donorbox.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.donorbox.data.model.MyDonations

@Dao
interface MyDonationsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDonations(myDonations: MyDonations)

    @Query("SELECT * FROM myDonations_table")
    suspend fun getAllDonations(): List<MyDonations>
}
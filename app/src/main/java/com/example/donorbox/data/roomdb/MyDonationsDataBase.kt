package com.example.donorbox.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.donorbox.data.model.MyDonations

@Database(
    entities = [MyDonations::class],
    version = 1,
    exportSchema = false
)
abstract class MyDonationsDataBase: RoomDatabase() {
    abstract fun myDonationsDAO(): MyDonationsDAO
}
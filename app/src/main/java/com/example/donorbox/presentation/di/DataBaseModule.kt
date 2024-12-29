package com.example.donorbox.presentation.di

import androidx.room.Room
import com.example.donorbox.data.roomdb.MyDonationsDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object DataBaseModule {
    val databaseModule = module {
        // Provide the MyDonationsDataBase instance
        single {
            Room.databaseBuilder(
                androidApplication(),
                MyDonationsDataBase::class.java,
                "MyDonationsApp"
            ).build()
        }

        // Provide the MyDonationsDAO instance
        single {
            get<MyDonationsDataBase>().myDonationsDAO()
        }
    }
}
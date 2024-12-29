package com.example.donorbox.presentation.di

import com.example.donorbox.data.dataSource.localdatabase.LocalDataBaseDataSource
import com.example.donorbox.data.dataSource.localdatabase.LocalDataBaseDataSourceImpl
import com.example.donorbox.data.dataSource.localdatabase.LocalDataBaseRepositoryImpl
import com.example.donorbox.domain.repository.LocalDataBaseRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LocalDataBaseModule {
    val localDataBaseModule = module {
        single<LocalDataBaseDataSourceImpl>{
            LocalDataBaseDataSourceImpl(
                myDonationsDAO = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<LocalDataBaseRepositoryImpl>{
            LocalDataBaseRepositoryImpl(
                localDataBaseDataSourceImpl = get()
            )
        }

        single<LocalDataBaseDataSource> {
            LocalDataBaseDataSourceImpl(
                myDonationsDAO = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<LocalDataBaseRepository> {
            LocalDataBaseRepositoryImpl(
                localDataBaseDataSourceImpl = get()
            )
        }
    }
}
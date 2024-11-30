package com.example.donorbox.presentation.di

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl
import com.example.donorbox.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object FirebaseModule {

    val firebaseModule = module {
        single(named("Dispatchers.IO")) { Dispatchers.IO }

        // Provide FirebaseAuth instance
        single {
            FirebaseAuth.getInstance()
        }

        single<DatabaseReference>(named("receiversReference")) {
            FirebaseDatabase.getInstance().getReference("receivers")
        }

        single<FirebaseAuthenticationDataSourceImpl> {
            FirebaseAuthenticationDataSourceImpl(
                auth = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<FirebaseReadDataDataSourceImpl> {
            FirebaseReadDataDataSourceImpl(
                databaseReference = get(named("receiversReference")),
                context = androidContext(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
            )
        }

        single<FirebaseWriteDataDataSourceImpl>{
            FirebaseWriteDataDataSourceImpl()
        }

        single<FirebaseRepositoryImpl> {
            FirebaseRepositoryImpl(get(), get())
        }


        single<FirebaseAuthenticationDataSource>{
            FirebaseAuthenticationDataSourceImpl(
                auth = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<FirebaseReadDataDataSource>{
            FirebaseReadDataDataSourceImpl(
                databaseReference = get(named("receiversReference")),
                context = androidContext(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
            )
        }

        single<FirebaseWriteDataDataSource>{
            FirebaseWriteDataDataSourceImpl()
        }

        single<FirebaseRepository>{
            FirebaseRepositoryImpl(get(), get())
        }
    }

}
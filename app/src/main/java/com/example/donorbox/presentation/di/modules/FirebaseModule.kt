package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSourceImpl
import com.example.donorbox.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object FirebaseModule {

    val firebaseModule = module {
        // Provide FirebaseAuth instance
        single {
            FirebaseAuth.getInstance()
        }

        single {
            FirebaseMessaging.getInstance()
        }

        single<DatabaseReference>(named("receiversReference")) {
            FirebaseDatabase.getInstance().getReference("receivers")
        }

        single<DatabaseReference>(named("usersReference")) {
            FirebaseDatabase.getInstance().getReference("users")
        }

        single<FirebaseAuthenticationDataSourceImpl> {
            FirebaseAuthenticationDataSourceImpl(
                auth = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<FirebaseReadDataSourceImpl> {
            FirebaseReadDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                context = androidContext(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                auth = get(),
            )
        }

        single<FirebaseWriteDataSourceImpl> {
            FirebaseWriteDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                coroutineDispatcher = get(named("Dispatchers.IO")),
            )
        }

        single<FirebaseNotificationDataSourceImpl>{
            FirebaseNotificationDataSourceImpl(
                firebaseMessaging = get(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                firebaseAuthenticationDataSourceImpl = get(),
                firebaseReadDataSourceImpl = get(),
                firebaseWriteDataSourceImpl = get(),
                fcmApi = get(),
            )
        }

        single<FirebaseRepositoryImpl> {
            FirebaseRepositoryImpl(get(), get(), get(),get())
        }


        single<FirebaseAuthenticationDataSource> {
            FirebaseAuthenticationDataSourceImpl(
                auth = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<FirebaseReadDataDataSource> {
            FirebaseReadDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                context = androidContext(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                auth = get(),
            )
        }

        single<FirebaseWriteDataDataSource> {
            FirebaseWriteDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                coroutineDispatcher = get(named("Dispatchers.IO")),
            )
        }

        single<FirebaseNotificationDataSource>{
            FirebaseNotificationDataSourceImpl(
                firebaseMessaging = get(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                firebaseAuthenticationDataSourceImpl = get(),
                firebaseReadDataSourceImpl = get(),
                firebaseWriteDataSourceImpl = get(),
                fcmApi = get(),
            )
        }

        single<FirebaseRepository> {
            FirebaseRepositoryImpl(get(), get(), get(),get())
        }
    }

}
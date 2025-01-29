package com.example.donorbox.presentation.di.modules

import com.example.donorbox.data.dataSource.firebase.FirebaseRepositoryImpl
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseNotification.FirebaseNotificationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSource
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataSource
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

        single<DatabaseReference>(named("contactUsReference")){
            FirebaseDatabase.getInstance().getReference("contact us")
        }

        single<FirebaseAuthenticationDataSource> {
            FirebaseAuthenticationDataSourceImpl(
                auth = get(),
                coroutineDispatcher = get(named("Dispatchers.IO"))
            )
        }

        single<FirebaseReadDataSource> {
            FirebaseReadDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                context = androidContext(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                auth = get(),
            )
        }

        single<FirebaseWriteDataSource> {
            FirebaseWriteDataSourceImpl(
                receiversDatabaseReference = get(named("receiversReference")),
                usersDatabaseReference = get(named("usersReference")),
                contactUsDatabaseReference = get(named("contactUsReference")),
                coroutineDispatcher = get(named("Dispatchers.IO")),
            )
        }

        single<FirebaseNotificationDataSource>{
            FirebaseNotificationDataSourceImpl(
                firebaseMessaging = get(),
                coroutineDispatcher = get(named("Dispatchers.IO")),
                auth = get(),
                receiversDatabaseReference = get(named("receiversReference")),
                context = androidContext(),
                fcmApi = get(),
            )
        }

        single<FirebaseRepository> {
            FirebaseRepositoryImpl(get(), get(), get(),get())
        }
    }

}
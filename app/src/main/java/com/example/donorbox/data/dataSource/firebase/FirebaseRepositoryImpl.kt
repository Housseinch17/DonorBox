package com.example.donorbox.data.dataSource.firebase

import com.example.donorbox.data.dataSource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseReadData.FirebaseReadDataDataSourceImpl
import com.example.donorbox.data.dataSource.firebase.firebaseWriteData.FirebaseWriteDataDataSourceImpl
import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import com.example.donorbox.presentation.sealedInterfaces.AuthState
import com.example.donorbox.presentation.sealedInterfaces.PasswordChangement
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse

class FirebaseRepositoryImpl(
    private val firebaseAuthenticationDataSourceImpl: FirebaseAuthenticationDataSourceImpl,
    private val firebaseReadDataDataSourceImpl: FirebaseReadDataDataSourceImpl,
    private val firebaseWriteDataDataSourceImpl: FirebaseWriteDataDataSourceImpl
): FirebaseRepository {
    override suspend fun getCurrentUser(): String? {
        return firebaseAuthenticationDataSourceImpl.getCurrentUser()
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseAuthenticationDataSourceImpl.logIn(email,password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseAuthenticationDataSourceImpl.signUp(email,password)
    }

    override suspend fun signOut() {
        return firebaseAuthenticationDataSourceImpl.signOut()
    }

    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement {
        return firebaseAuthenticationDataSourceImpl.changePassword(email,newPassword)
    }

    override suspend fun resetPassword(email: String): PasswordChangement {
        return firebaseAuthenticationDataSourceImpl.resetPassword(email)
    }

    override suspend fun readReceivers(): ReceiversResponse {
        return firebaseReadDataDataSourceImpl.readReceivers()
    }

    override suspend fun writeToken(username: String, token: String) {
        return firebaseWriteDataDataSourceImpl.writeToken(username,token)
    }

    override suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) {
        firebaseAuthenticationDataSourceImpl.verifyPassword(password,onVerified,setError)
    }

    override suspend fun getAllReceivers(): List<String> {
        return firebaseReadDataDataSourceImpl.getAllReceivers()
    }

}
package com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase

import com.example.donorbox.domain.repository.FirebaseRepository
import com.example.donorbox.presentation.screens.authentication.PasswordChangement
import com.example.donorbox.presentation.screens.login.AuthState
import com.example.donorbox.presentation.screens.signup.AccountStatus

class AuthenticationUseCaseImpl(
    private val firebaseRepository: FirebaseRepository
): AuthenticationUseCase {
    override suspend fun changePassword(email: String, newPassword: String): PasswordChangement =
        firebaseRepository.changePassword(email, newPassword)


    override suspend fun getCurrentUser(): String? = firebaseRepository.getCurrentUser()

    override suspend fun logIn(email: String, password: String): AuthState = firebaseRepository.logIn(email,password)

    override suspend fun resetPassword(email: String): PasswordChangement = firebaseRepository.resetPassword(email)

    override suspend fun signOut() = firebaseRepository.signOut()

    override suspend fun signUp(email: String, password: String): AccountStatus = firebaseRepository.signUp(email,password)

    override suspend fun verifyPassword(
        password: String,
        onVerified: () -> Unit,
        setError: (String) -> Unit
    ) = firebaseRepository.verifyPassword(password, onVerified, setError)
}
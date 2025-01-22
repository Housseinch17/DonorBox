package com.example.donorbox.presentation.screens.signup

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.AuthenticationUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseWriteDataUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AccountStatus {
    data class IsCreated(val message: String) : AccountStatus
    data object NotCreated : AccountStatus
    data object IsLoading : AccountStatus
    data class Error(val error: String) : AccountStatus
}

sealed interface SignUpAction{
    data class SignUp(val name: String, val email: String, val password: String, val confirmPassword: String): SignUpAction
    data class SetEmail(val emailValue: String): SignUpAction
    data class SetName(val nameValue: String): SignUpAction
    data class SetPassword(val passwordValue: String): SignUpAction
    data class SetConfirmPassword(val confirmPasswordValue: String): SignUpAction
    data object SetShowPassword: SignUpAction
    data object SetShowConfirmPassword: SignUpAction
}

class SignUpViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val firebaseWriteDataUseCase: FirebaseWriteDataUseCase,
) : ViewModel() {
    private val _signupUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signupUiState: StateFlow<SignUpUiState> = _signupUiState.asStateFlow()


    private val _eventMessage: Channel<String> = Channel()
    val eventMessage = _eventMessage.receiveAsFlow()

    init {
        Log.d("ViewModelInitialization", "SignupViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "SignupViewModel destroyed")
    }

    fun onActionSignUp(signUpAction: SignUpAction){
        when(signUpAction){
            is SignUpAction.SetEmail -> setEmail(signUpAction.emailValue)
            is SignUpAction.SetName -> setName(signUpAction.nameValue)
            is SignUpAction.SetConfirmPassword -> setConfirmPassword(signUpAction.confirmPasswordValue)
            is SignUpAction.SetPassword -> setPassword(signUpAction.passwordValue)
            SignUpAction.SetShowConfirmPassword -> setShowConfirmPassword()
            SignUpAction.SetShowPassword -> setShowPassword()
            is SignUpAction.SignUp -> signUp(
                name = signUpAction.name,
                email = signUpAction.email,
                password = signUpAction.password,
                confirmPassword = signUpAction.confirmPassword
            )
        }
    }

    fun setAlreadyHaveAccountButton() {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(alreadyHaveAccountButton = false)
            }
            delay(200L)
            _signupUiState.update { newState ->
                newState.copy(alreadyHaveAccountButton = true)
            }
        }
    }


    private fun emitError(message: String) {
        viewModelScope.launch {
            _eventMessage.send(message)
        }
    }

    private fun signUp(email: String, password: String, name: String, confirmPassword: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(accountStatus = AccountStatus.IsLoading)
            }
            if (name.isEmpty()) {
                emitError("Please fill name")
                _signupUiState.update { newState ->
                    newState.copy(accountStatus = AccountStatus.NotCreated)
                }
            } else {
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    emitError("All inputs should be filled!")
                    _signupUiState.update { newState ->
                        newState.copy(accountStatus = AccountStatus.NotCreated)
                    }
                } else if (password.length < 6) {
                    emitError("Password should contains minimum 6 letters!")
                    _signupUiState.update { newState ->
                        newState.copy(accountStatus = AccountStatus.NotCreated)
                    }
                } else {
                    if(password == confirmPassword){
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            val response = authenticationUseCase.signUp(email, password)
                            if (response is AccountStatus.Error) {
                                emitError(response.error)
                                _signupUiState.update { newState ->
                                    newState.copy(accountStatus = AccountStatus.NotCreated)
                                }
                            } else if (response is AccountStatus.IsCreated) {
                                _signupUiState.update { newState ->
                                    newState.copy(accountStatus = AccountStatus.IsCreated(response.message))
                                }
                                emitError(response.message)
                                firebaseWriteDataUseCase.addUser(username = email, name = name)
                            }
                        } else {
                            emitError("Please use a valid email account!")
                            _signupUiState.update { newState ->
                                newState.copy(accountStatus = AccountStatus.NotCreated)
                            }
                        }
                    }else{
                        emitError("Password and Confirm password should be the same!")
                        _signupUiState.update { newState ->
                            newState.copy(accountStatus = AccountStatus.NotCreated)
                        }
                    }
                }
            }
            Log.d("MyTag", _signupUiState.value.accountStatus.toString())
        }

    }

    private fun setEmail(email: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(email = email)
            }
        }
    }

    private fun setName(name: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(name = name)
            }
        }
    }

    private fun setConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(confirmPassword = confirmPassword)
            }
        }
    }

    private fun setPassword(password: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(password = password)
            }
        }
    }

    private fun setShowPassword() {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    private fun setShowConfirmPassword() {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(
                    showConfirmPassword = !newState.showConfirmPassword,
                )
            }
        }
    }

}
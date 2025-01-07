package com.example.donorbox.presentation.screens.signup

import android.util.Log
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseAuthenticationUseCase.SignUpUseCase
import com.example.donorbox.domain.useCase.firebaseUseCase.firebaseWriteDataUseCase.FirebaseAddUserUseCase
import com.example.donorbox.presentation.sealedInterfaces.AccountStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val addUserUseCase: FirebaseAddUserUseCase,
) : ViewModel() {
    private val _signupUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signupUiState: StateFlow<SignUpUiState> = _signupUiState.asStateFlow()

    private val _signUpSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val signUpSharedFlow: SharedFlow<String> = _signUpSharedFlow.asSharedFlow()

    init {
        Log.d("ViewModelInitialization", "SignupViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "SignupViewModel destroyed")
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
            _signUpSharedFlow.emit(message)
        }
    }

    fun signUp(email: String, password: String, name: String, confirmPassword: String) {
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
                            val response = signUpUseCase.signUp(email, password)
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
                                addUserUseCase.addUser(username = email, name = name)
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

    fun setEmail(email: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(email = email)
            }
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(name = name)
            }
        }
    }

    fun setConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(confirmPassword = confirmPassword)
            }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(password = password)
            }
        }
    }

    fun setShowPassword() {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    fun setShowConfirmPassword() {
        viewModelScope.launch {
            _signupUiState.update { newState ->
                newState.copy(
                    showConfirmPassword = !newState.showConfirmPassword,
                )
            }
        }
    }

    fun getIconVisibility(): ImageVector {
        val showPassword = _signupUiState.value.showPassword
        return if (showPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }

    fun getConfirmIconVisibility(): ImageVector {
        val confirmShowPassword = _signupUiState.value.showConfirmPassword
        return if (confirmShowPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }
}
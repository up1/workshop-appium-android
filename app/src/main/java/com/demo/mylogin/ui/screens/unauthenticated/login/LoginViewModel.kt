package com.demo.mylogin.ui.screens.unauthenticated.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.mylogin.data.remote.LoginRequest
import com.demo.mylogin.repository.Resource
import com.demo.mylogin.repository.UserRepository
import com.demo.mylogin.ui.common.state.ErrorState
import com.demo.mylogin.ui.screens.unauthenticated.login.state.LoginErrorState
import com.demo.mylogin.ui.screens.unauthenticated.login.state.LoginState
import com.demo.mylogin.ui.screens.unauthenticated.login.state.LoginUiEvent
import com.demo.mylogin.ui.screens.unauthenticated.login.state.emailOrMobileEmptyErrorState
import com.demo.mylogin.ui.screens.unauthenticated.login.state.loginErrorState
import com.demo.mylogin.ui.screens.unauthenticated.login.state.passwordEmptyErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var loginState = mutableStateOf(LoginState())
        private set

    /**
     * Function called on any login event [LoginUiEvent]
     */
    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {

            // Email/Mobile changed
            is LoginUiEvent.EmailOrMobileChanged -> {
                loginState.value = loginState.value.copy(
                    emailOrMobile = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        emailOrMobileErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            emailOrMobileEmptyErrorState
                    )
                )
            }

            // Password changed
            is LoginUiEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            passwordEmptyErrorState
                    )
                )
            }

            // Submit Login
            is LoginUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    // TODO Trigger login in authentication flow
                    val request =
                        LoginRequest(loginState.value.emailOrMobile, loginState.value.password);
                    viewModelScope.launch {
                        login(request);
                    }
                }
            }

            is LoginUiEvent.ErrorDismissed -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState())
            }
        }
    }

    suspend fun login(request: LoginRequest) {
        val result = userRepository.login(request);
        if (result is Resource.Success) {
            loginState.value = loginState.value.copy(isLoginSuccessful = true)
        } else {
            loginState.value = loginState.value.copy(
                isLoginSuccessful = false,
                errorState = loginState.value.errorState.copy(loginErrorState = loginErrorState) )
        }
    }

    /**
     * Function to validate inputs
     * Ideally it should be on domain layer (usecase)
     * @return true -> inputs are valid
     * @return false -> inputs are invalid
     */
    private fun validateInputs(): Boolean {
        val emailOrMobileString = loginState.value.emailOrMobile.trim()
        val passwordString = loginState.value.password
        return when {

            // Email/Mobile empty
            emailOrMobileString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        emailOrMobileErrorState = emailOrMobileEmptyErrorState
                    )
                )
                false
            }

            //Password Empty
            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }

            // No errors
            else -> {
                // Set default error state
                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }

}
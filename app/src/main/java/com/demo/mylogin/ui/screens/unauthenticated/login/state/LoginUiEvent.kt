package com.demo.mylogin.ui.screens.unauthenticated.login.state

/**
 * Login Screen Events
 */
sealed class LoginUiEvent {
    data class EmailOrMobileChanged(val inputValue: String) : LoginUiEvent()
    data class PasswordChanged(val inputValue: String) : LoginUiEvent()
    data object Submit : LoginUiEvent()
    data object ErrorDismissed : LoginUiEvent()
}
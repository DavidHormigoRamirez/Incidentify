package com.alaturing.incidentify.login.ui

sealed class LoginUiState {
    data object Initial:LoginUiState()
    data object LoggingIn:LoginUiState()
    data object LoggedIn:LoginUiState()
    data class Error(val errorMessage:String):LoginUiState()
}
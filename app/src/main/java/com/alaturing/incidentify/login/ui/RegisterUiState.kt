package com.alaturing.incidentify.login.ui

sealed class RegisterUiState {
    data object Initial:RegisterUiState()
    data object Registering:RegisterUiState()
    data object Registered:RegisterUiState()
    data class Error(val errorMessage:String):RegisterUiState()
}
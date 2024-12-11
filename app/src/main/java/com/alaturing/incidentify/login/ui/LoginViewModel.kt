package com.alaturing.incidentify.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()

    fun login(user:String,password:String) {

        viewModelScope.launch {
            _uiState.value = LoginUiState.LoggingIn
            delay(3000L)
            _uiState.value = LoginUiState.LoggedIn
        }
    }


}
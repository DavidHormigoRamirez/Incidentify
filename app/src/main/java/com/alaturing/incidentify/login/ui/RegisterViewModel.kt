package com.alaturing.incidentify.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alaturing.incidentify.login.data.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository:IUserRepository
): ViewModel()
{
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState:StateFlow<RegisterUiState>
        get() = _uiState.asStateFlow()

    fun onRegister(userName:String,email:String,password:String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Registering
            delay(2000L)
            if (repository.register(userName,email,password)==null) {
                _uiState.value = RegisterUiState.Error("User cannot be registered")
            }
            else {
                _uiState.value = RegisterUiState.Registered
            }
        }
    }

}
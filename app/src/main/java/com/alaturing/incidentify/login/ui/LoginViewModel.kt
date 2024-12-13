package com.alaturing.incidentify.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alaturing.incidentify.login.data.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Gestor de estado para la IU de login
 * Publíca el estado en uiState
 *
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: IUserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()

    /**
     * @param identifier Correo o identificador del usuario
     * @param password Contraseña
     */
    fun onLogin(identifier:String, password:String) {

        viewModelScope.launch {
            _uiState.value = LoginUiState.LoggingIn

            val user = userRepository.login(identifier,password)
            if (user!=null) {
                _uiState.value = LoginUiState.LoggedIn
            }
            else
            {
                _uiState.value = LoginUiState.Error("Incorrect user or password")
            }

        }
    }


}
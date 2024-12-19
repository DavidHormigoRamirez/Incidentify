package com.alaturing.incidentify.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alaturing.incidentify.authentication.data.repository.UserRepository
import com.alaturing.incidentify.main.incident.data.repository.IncidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Implemetación de [ViewModel] para gestionar el fragmento de Home
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: UserRepository,
    private val repository: IncidentRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Success(0,0))
    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repository.observeAll().collect {
                result ->
                    if (result.isSuccess) {
                        val incidents = result.getOrNull()
                        if (incidents!=null) {
                            val unResolved = incidents.count { i -> !i.solved }
                            val reported = incidents.count()
                            _uiState.value = HomeUiState.Success(
                                unresolvedIncidents = unResolved,
                                reportedIncidents = reported
                            )
                        }
                    }
                    else {
                        _uiState.value = HomeUiState.Error("no se ha podido recuperar")
                    }

            }
        }

    }
    fun onLogout() {
        viewModelScope.launch {
            loginRepository.logout()
            _uiState.value = HomeUiState.LoggedOut
        }
    }

}
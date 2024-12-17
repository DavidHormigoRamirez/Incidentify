package com.alaturing.incidentify.main.incident.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alaturing.incidentify.main.incident.data.repository.IncidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Implementación de [ViewModel] para gestionar la lista de incidentes
 *
 */
@HiltViewModel
class IncidentsViewModel @Inject constructor(
    private val repository: IncidentRepository
):ViewModel() {

    private val _uiState = MutableStateFlow<IncidentsUiState>(IncidentsUiState.Initial)
    val uiState: StateFlow<IncidentsUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect {
                incidents ->
                    if (incidents.isSuccess) {
                        _uiState.value = IncidentsUiState.Success(incidents.getOrNull()!!)
                    }
                    else
                    {
                        // TODO
                        _uiState.value = IncidentsUiState.Error("Error recuperando")
                    }

            }
        }
    }
}
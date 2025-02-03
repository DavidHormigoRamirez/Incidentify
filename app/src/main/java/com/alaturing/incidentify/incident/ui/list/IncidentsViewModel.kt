package com.alaturing.incidentify.incident.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alaturing.incidentify.incident.data.repository.IncidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Implementación de [ViewModel] para gestionar la lista de incidentes
 *
 */
@HiltViewModel
class IncidentsViewModel @Inject constructor(
    repository: IncidentRepository
):ViewModel() {

    // Como no manipilamos nunca el flujo, salvo convertir a estado de pantalla
    // usamos esta sintaxis
    val uiState: StateFlow<IncidentsUiState> = repository.observeAll().map {
        result ->
            if (result.isSuccess) {
                IncidentsUiState.Success(result.getOrNull()!!)
            }
            else {
                IncidentsUiState.Error(result.exceptionOrNull()!!.message!!)
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        IncidentsUiState.Initial
    )
}
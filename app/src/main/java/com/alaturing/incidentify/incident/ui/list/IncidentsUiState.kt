package com.alaturing.incidentify.incident.ui.list

import com.alaturing.incidentify.incident.model.Incident

sealed class IncidentsUiState {
    data object Initial: IncidentsUiState()
    data object Loading: IncidentsUiState()
    data class Error(val message:String): IncidentsUiState()
    data class Success(val incidents:List<Incident>): IncidentsUiState()
}
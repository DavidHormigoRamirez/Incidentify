package com.alaturing.incidentify.main.home

/**
 * Clase sellada para repreentar el estado de la ventana de home
 */
sealed class HomeUiState {
    data object Loading: HomeUiState()
    data object LoggedOut: HomeUiState()
    data class Success(val unresolvedIncidents:Int, val reportedIncidents:Int):HomeUiState()
    data class Error(val message:String):HomeUiState()

}
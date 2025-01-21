package com.alaturing.incidentify.main.incident.ui.edit

import android.net.Uri
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
 * Implementación  de [ViewModel] para almacenar los datos de la edición de
 * incidentes
 */
@HiltViewModel
class IncidentEditViewModel @Inject constructor(
    private val repository: IncidentRepository
): ViewModel()  {

    private val _uiState = MutableStateFlow<IncidentEditUiState>(IncidentEditUiState.New)
    val uiState:StateFlow<IncidentEditUiState>
        get() = _uiState.asStateFlow()

    private val _photo = MutableStateFlow<Uri>(Uri.EMPTY)
    val photo: StateFlow<Uri>
        get() = _photo.asStateFlow()

    /**
     * Función para poner en el flujo la uri de la ultima foto capturada
     * @param uri [Uri] de la foto que apunta al archivo local
     */
    fun onImageCaptured(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                _photo.value = uri
            }
        }

    }

    fun onSaveNewIncident(description:String,evidence:Uri?) {
        viewModelScope.launch {
            val result = repository.createOne(description,evidence)
            if (result.isSuccess) {
                // TODO CAmbiar para emitir todo el incidnete
                _uiState.value = IncidentEditUiState.Created(result.getOrNull()!!.id)
            }
        }

    }

}
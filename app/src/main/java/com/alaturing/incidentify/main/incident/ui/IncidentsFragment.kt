package com.alaturing.incidentify.main.incident.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.alaturing.incidentify.R
import com.alaturing.incidentify.databinding.FragmentIncidentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
*/
@AndroidEntryPoint
class IncidentsFragment : Fragment() {

    private lateinit var binding:FragmentIncidentsBinding
    private val viewModel:IncidentsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIncidentsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = IncidentsAdapter()
        val rv = binding.incidentRv
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when(uiState) {
                        is IncidentsUiState.Error -> {
                            // TODO
                        }
                        IncidentsUiState.Initial -> {
                            // TODO
                        }
                        IncidentsUiState.Loading -> {
                            // TODO añadir Circular
                        }
                        is IncidentsUiState.Success -> {
                            (rv.adapter as IncidentsAdapter).submitList(uiState.incidents)
                        }
                    }
                }
            }
        }
        // Manejador para el boton de crear incidente, ejecutamos la navegación
        binding.createIncidentFab.setOnClickListener {
            findNavController().navigate(IncidentsFragmentDirections.actionIncidentsFragmentToIncidentEditFragment())
        }

    }


}
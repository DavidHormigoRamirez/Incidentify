package com.alaturing.incidentify.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alaturing.incidentify.R
import com.alaturing.incidentify.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * [Fragment] para mostrar los datos de resumen
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState ->
                        when(uiState) {
                            is HomeUiState.Error -> {}
                            is HomeUiState.Success -> {
                                binding.reportedCount.text = resources.getQuantityString(R.plurals.reported,uiState.reportedIncidents,uiState.reportedIncidents)
                                binding.unresolvedCount.text = resources.getQuantityString(R.plurals.unresolved,uiState.unresolvedIncidents,uiState.unresolvedIncidents)

                            }
                        }
                }
            }
        }
    }

}
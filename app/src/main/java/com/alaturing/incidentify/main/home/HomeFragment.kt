package com.alaturing.incidentify.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alaturing.incidentify.R
import com.alaturing.incidentify.authentication.ui.AuthenticationActivity
import com.alaturing.incidentify.databinding.FragmentHomeBinding
import com.alaturing.incidentify.main.NavigationSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * [Fragment] para mostrar los datos de resumen
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    //private val viewModel: HomeViewModel by navGraphViewModels(R.id.main_xml)
    private val navViewModel: NavigationSharedViewModel by activityViewModels()

    init {
        Log.d("HomeViewModel", "ViewModel instance created")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setOnMenuItemClickListener {
            mi -> when(mi.itemId) {
                R.id.logout -> {
                    viewModel.onLogout()
                    true
                }
                else -> false
            }

        }
        binding.toUnresolvedIncidents.setOnClickListener {

            //val action = HomeFragmentDirections.actionHomeToIncident()
            //findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState ->
                        when(uiState) {
                            is HomeUiState.Loading -> {}
                            is HomeUiState.Error -> {}
                            is HomeUiState.Success -> {
                                binding.reportedCount.text = resources.getQuantityString(R.plurals.reported,uiState.reportedIncidents,uiState.reportedIncidents)
                                binding.unresolvedCount.text = resources.getQuantityString(R.plurals.unresolved,uiState.unresolvedIncidents,uiState.unresolvedIncidents)

                            }
                            HomeUiState.LoggedOut -> {
                                val intent = Intent(requireContext(),AuthenticationActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP )
                                startActivity(intent)
                            }
                        }
                }
            }
        }
    }

}
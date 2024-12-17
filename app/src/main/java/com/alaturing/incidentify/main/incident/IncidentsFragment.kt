package com.alaturing.incidentify.main.incident

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alaturing.incidentify.databinding.FragmentIncidentsBinding

/**
 * A simple [Fragment] subclass.
*/
class IncidentsFragment : Fragment() {

    private lateinit var binding:FragmentIncidentsBinding


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


}
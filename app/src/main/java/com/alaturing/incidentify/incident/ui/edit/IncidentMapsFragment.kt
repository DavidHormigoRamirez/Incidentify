package com.alaturing.incidentify.incident.ui.edit

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.alaturing.incidentify.R
import com.alaturing.incidentify.databinding.FragmentIncidentMapsBinding
import com.alaturing.incidentify.incident.data.repository.IncidentRepository

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IncidentMapsFragment @Inject constructor(

) : Fragment() {
    @Inject
    lateinit var repository:IncidentRepository
    private val args: IncidentMapsFragmentArgs by navArgs()
    private lateinit var binding: FragmentIncidentMapsBinding
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        val id = args.incidentId
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repository.readOne(id)
            if (result.isSuccess) {
                val incident = result.getOrNull()
                val currentLocation = LatLng(incident!!.latitude!!,
                    incident.longitude!!)
                googleMap.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .title(incident.description))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIncidentMapsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
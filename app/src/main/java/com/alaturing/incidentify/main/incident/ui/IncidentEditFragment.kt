package com.alaturing.incidentify.main.incident.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.alaturing.incidentify.databinding.FragmentIncidentEditBinding
import dagger.hilt.android.AndroidEntryPoint

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA,
                                            Manifest.permission.RECORD_AUDIO,)

@AndroidEntryPoint
class IncidentEditFragment : Fragment() {

    private lateinit var binding: FragmentIncidentEditBinding

    // PEDIMOS PERMISOS
    private val contract = ActivityResultContracts.RequestMultiplePermissions()
    private val launcher = registerForActivityResult(contract) {
            permissions ->
        var hasPermissions = true
        permissions.entries.forEach { permission ->
            if (permission.key in PERMISSIONS_REQUIRED && !permission.value) {
                hasPermissions = false
            }
        }
        if (!hasPermissions) {
            // TODO, Mensaje de error no tiene permisos
            Toast.makeText(requireContext(),"No tienes permisos",Toast.LENGTH_LONG).show()
        }
        else {
            navigateToCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncidentEditBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun navigateToCamera() {
        // TODO
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Manejador del boton de tomar foto
        // Esto debe navegar a la vista de camara, por lo que antes de realizarlo vamos a comprobar
        // que tiene permisos para la camara
        binding.showCameraBtn.setOnClickListener {

            // SI TENEMOS PERMISOS NAVEGAMOS A LA CAMARA
            if (hasCameraPermissions(requireContext())) {
                navigateToCamera()
            }
            else {
                // SI NO TENEMOS PERMISOS, LOS PEDIMOS
                launcher.launch(PERMISSIONS_REQUIRED)
            }
        }
    }

    private fun hasCameraPermissions(context: Context):Boolean {
        // Tenemos permiso si todos los permisos han sido concecidos
        return PERMISSIONS_REQUIRED.all { p ->
            ContextCompat.checkSelfPermission(context,p) == PackageManager.PERMISSION_GRANTED
        }

    }

}
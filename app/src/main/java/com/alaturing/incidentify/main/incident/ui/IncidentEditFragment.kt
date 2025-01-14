package com.alaturing.incidentify.main.incident.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load

import com.alaturing.incidentify.databinding.FragmentIncidentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA,
                                            Manifest.permission.RECORD_AUDIO,)

@AndroidEntryPoint
class IncidentEditFragment : Fragment() {

    private val viewModel:IncidentEditViewModel by activityViewModels()
    private lateinit var binding: FragmentIncidentEditBinding

    // Contrato de permisos múltiples
    private val contract = ActivityResultContracts.RequestMultiplePermissions()
    // Registramos el fragmento para gestionar la elección de permisos del usuario
    private val launcher = registerForActivityResult(contract) {
            permissions ->
        var hasPermissions = true
        permissions.entries.forEach { permission ->
            if (permission.key in PERMISSIONS_REQUIRED && !permission.value) {
                hasPermissions = false
            }
        }
        if (!hasPermissions) {
            Toast.makeText(requireContext(),"No tienes permisos",Toast.LENGTH_LONG).show()
        }
        else {
            navigateToCamera()
        }
    }

    // Registramos el fragmento como receptor de un seleccionador de media
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Si la uril no es nula, es que el usuario ha selccionado algín archivo
        if (uri != null) {
            // Lo carcagmos en el ImageView
            binding.incidentImage.load(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Manejador del boton de tomar foto
        // Esto debe navegar a la vista de camara, por lo que antes de realizarlo vamos a comprobar
        // que tiene permisos para la camara
        binding.showCameraBtn.setOnClickListener {
            //onShowMediaSelector()

            // SI TENEMOS PERMISOS NAVEGAMOS A LA CAMARA
            if (hasCameraPermissions(requireContext())) {
                navigateToCamera()
            }
            else {
                // SI NO TENEMOS PERMISOS, LOS PEDIMOS
                launcher.launch(PERMISSIONS_REQUIRED)
            }
        }

        // MAnejador para el boton de selecionar
        binding.selectPhotoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collect {
                    photoUri ->
                        when(photoUri) {
                            Uri.EMPTY -> {
                                //No hay foto, podemos poner un placeholder en la imagen
                            }
                            else -> {
                                // tenemos foto, la ponemos en la UI aprovechando
                                // que tenemos Coil
                                binding.incidentImage.load(photoUri)
                            }
                        }
                }
            }
        }
    }

    /**
     * Función para comprobar si se tienen todos los permisos requeridos
     * para usar la camara
     * @param context Contexto de la aplicación
     * @return Si tiene permisos para usar la camara
     */
    private fun hasCameraPermissions(context: Context):Boolean {
        // Tenemos permiso si todos los permisos han sido concecidos
        return PERMISSIONS_REQUIRED.all { p ->
            ContextCompat.checkSelfPermission(context,p) == PackageManager.PERMISSION_GRANTED
        }

    }

    /**
     * Función que navega al fragmento de Preview de camara
     */
    private fun navigateToCamera() {
        val action = IncidentEditFragmentDirections.actionIncidentEditFragmentToCameraPreviewFragment()
        findNavController().navigate(action)
    }



}
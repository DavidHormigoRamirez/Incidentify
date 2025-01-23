package com.alaturing.incidentify.main.incident.ui.edit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load
import com.alaturing.incidentify.databinding.FragmentIncidentEditBinding
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class IncidentEditFragment @Inject constructor() : Fragment() {

    private var _photoUri:Uri? = null
    private val viewModel: IncidentEditViewModel by activityViewModels()
    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentIncidentEditBinding
    private val cameraPermissionContract = ActivityResultContracts.RequestPermission()
    private val cameraPermissionLauncher = registerForActivityResult(cameraPermissionContract) {
        isGranted ->
            if (isGranted)
                navigateToCamera()
            else
                Toast.makeText(requireContext(),
                    "No hay permisos para la camara",
                    Toast.LENGTH_LONG,
                    ).show()
    }
    // Contrato de permisos múltiples
    private val mapContract = ActivityResultContracts.RequestMultiplePermissions()

    // Registramos el fragmento para gestionar la elección de permisos del usuario
    private val mapPermissionLauncher = registerForActivityResult(mapContract) {
            permissions ->
        var hasPermissions = true
        permissions.entries.forEach { permission ->
            if (permission.key in MAP_REQUIRED_PERMISSIONS  && !permission.value) {
                hasPermissions = false
            }
        }
        // SILENCIOSO
        if (!hasPermissions) {
            Toast.makeText(requireContext(), "No tienes permisos", Toast.LENGTH_LONG).show()
        }
    }


    // Registramos el fragmento como receptor de un seleccionador de media
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Si la uril no es nula, es que el usuario ha selccionado algín archivo
        if (uri != null) {
            // Lo carcagmos en el ImageView
            //binding.incidentImage.load(uri)
            loadPhoto(uri)
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


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vamos a pedir permisos para la localización
        mapPermissionLauncher.launch(MAP_REQUIRED_PERMISSIONS)

        // Control estado de la pantalla
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState ->
                    when(uiState) {
                        is IncidentEditUiState.Created -> {
                            // Se ha creado el incidente, volvemos
                            findNavController().popBackStack()
                        }
                        is IncidentEditUiState.Error -> {

                        }
                        IncidentEditUiState.New -> {

                        }
                    }
                }
            }
        }


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
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        // MAnejador para el boton de selecionar
        binding.selectPhotoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Manejador evento de creación
        binding.saveIncidentBtn.setOnClickListener {
            var currentLocation: Location? = null
            viewLifecycleOwner.lifecycleScope.launch {

                if (hasMapPermissions()) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->

                            viewModel.onSaveNewIncident(
                                binding.incidentDescriptionInput.text.toString(),
                                _photoUri,
                                location,)
                        }
                }
                else {
                    viewModel.onSaveNewIncident(
                        binding.incidentDescriptionInput.text.toString(),
                        _photoUri,)
                }
                }

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
                                loadPhoto(photoUri)
                            }
                        }
                }
            }
        }
    }

    private fun loadPhoto(uri:Uri?) {
        binding.incidentImage.load(uri)
        _photoUri = uri
    }

    /**
     * Función para comprobar si se tienen todos los permisos requeridos
     * para usar la camara
     * @param context Contexto de la aplicación
     * @return Si tiene permisos para usar la camara
     */
    private fun hasCameraPermissions(context: Context) =
        // Tenemos permiso si todos los permisos han sido concecidos
        //return PERMISSIONS_REQUIRED.all { p ->
        //    ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED
        //}
        ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    /**
     * Función que navega al fragmento de Preview de camara
     */
    private fun navigateToCamera() {
        val action = IncidentEditFragmentDirections.actionIncidentEditFragmentToCameraPreviewFragment()
        findNavController().navigate(action)
    }

    private fun hasMapPermissions():Boolean {
        // Tenemos permiso si todos los permisos han sido concecidos
        return MAP_REQUIRED_PERMISSIONS.all { p ->
            ContextCompat.checkSelfPermission(requireContext(), p) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        val  MAP_REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
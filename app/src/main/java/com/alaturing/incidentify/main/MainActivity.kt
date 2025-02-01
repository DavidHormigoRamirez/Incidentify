package com.alaturing.incidentify.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alaturing.incidentify.R
import com.alaturing.incidentify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.app.NotificationChannel
/**
 * Implementación de [AppCompatActivity] para albergar la funcionalidad general de la aplicación
 * Vamos a habilitar el envio de notificaciones
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val notificationsPermissionContract = ActivityResultContracts.RequestPermission()
    private val notificationsPermissionLauncher = registerForActivityResult(notificationsPermissionContract) { isGranted ->
        if (isGranted) {

        }
    }



    private lateinit var binding:ActivityMainBinding
    private lateinit var navViewModel: NavigationSharedViewModel
    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        /**
         * Ask for permissions to send notifications
         */
        notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        registerNotificationChannel()

        // Bottom navigation setup
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_navigation_area) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBottomNav.setupWithNavController(navController)

        navViewModel = ViewModelProvider(this)[NavigationSharedViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                navViewModel.navigationEvents.collect { event ->
                    when (event) {
                        is NavigationEvent.ToIncidents -> {
                            navController.navigate(R.id.incident,null,
                                NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build())

                        }
                        is NavigationEvent.ToHome -> {
                        }
                    }
                }
            }
        }

        // We capture every navigation to check if the bottom toolbar must be hidden
        navController.addOnDestinationChangedListener {
            _,destination,_ ->
                val hideNavbar = destination.arguments["hideNavbar"]
            binding.mainBottomNav.isVisible = true
            hideNavbar?.let {
                binding.mainBottomNav.isVisible = false
            }

        }
    }

    /**
     * Function to register the app general notification channel
     */
    private fun registerNotificationChannel() {
            // Create the NotificationChannel.

            val name = getText(R.string.general_channel_name)
            val descriptionText = getText(R.string.general_channel_desc).toString()
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

    }
    companion object {
        const val CHANNEL_ID = "incidentify_default"
    }
}
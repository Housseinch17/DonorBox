package com.example.donorbox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.screens.main.MainPage
import com.example.donorbox.presentation.screens.main.MainViewModel
import com.example.donorbox.presentation.theme.DonorBoxTheme
import com.example.donorbox.presentation.util.requestPhoneCallPermission
import com.google.firebase.FirebaseApp
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()

        Log.d("MyTag", "onCreate started")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestingPhoneCallPermission()
        requestNotificationPermission()
        val restoreState = savedInstanceState?.getBundle("nav_state")
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            DonorBoxTheme {
                navController = rememberNavController()
                navController.restoreState(restoreState)
                val mainViewModel = koinViewModel<MainViewModel>()
                val mainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()
                if (mainUiState.currentScreen != NavigationScreens.Loading) {
                    Log.d("MyTag", "onCreate finished in ${System.currentTimeMillis() - startTime} ms")
                    MainPage(
                        navController = navController,
                        startDestination = mainUiState.currentScreen
                    )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //saving state when app is in background
        //if the application kills activity and recreate it when it comes from background
        //navController will be saved which means the navController is restoring it's state instead of the initial state.
        if (::navController.isInitialized) {
            outState.putBundle("nav_state", navController.saveState())
        }
    }

    //request for post notification permission
    private fun requestNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun requestingPhoneCallPermission() {
        requestPhoneCallPermission()
    }

}
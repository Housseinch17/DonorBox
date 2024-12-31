package com.example.donorbox

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.donorbox.presentation.screens.main.MainPage
import com.example.donorbox.presentation.theme.DonorBoxTheme
import com.google.firebase.FirebaseApp
import android.Manifest

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()

        Log.d("MainActivity", "onCreate started")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val restoreState = savedInstanceState?.getBundle("nav_state")
        enableEdgeToEdge()
        setContent {
            DonorBoxTheme {
                FirebaseApp.initializeApp(this)
                navController = rememberNavController()
                navController.restoreState(restoreState)
                MainPage(navController = navController)
                Log.d(
                    "MainActivity",
                    "onCreate finished in ${System.currentTimeMillis() - startTime} ms"
                )
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //saving state when app is in background
        if (::navController.isInitialized) {
            outState.putBundle("nav_state", navController.saveState())
        }
    }
}
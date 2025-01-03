package com.example.donorbox

import android.Manifest
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

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()

        Log.d("MainActivity", "onCreate started")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestPhoneCallPermission()
        requestNotificationPermission()
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

    //request for post notification permission
    private fun requestNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0)
            }
        }
    }

    private fun requestPhoneCallPermission(){
            val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                    1)
            }else{
                Log.d("Permissions", "CALL_PHONE permission already granted")
            }
    }

}
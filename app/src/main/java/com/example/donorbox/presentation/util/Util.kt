package com.example.donorbox.presentation.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.donorbox.presentation.navigation.NavigationScreens

// Extension function to check internet connectivity
@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return false

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

fun Activity.requestPhoneCallPermission() {
    val hasPermission =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
    if (!hasPermission) {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CALL_PHONE),
            1
        )
        Log.d("Permissions", "CALL_PHONE permission not granted")
    } else {
        Log.d("Permissions", "CALL_PHONE permission already granted")
    }
}


fun Context.callPhoneDirectly(
    phoneNumber: String,
    permissionDenied: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Permission granted, make the call
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    } else {
        permissionDenied()
    }
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true  // App is installed
    } catch (e: PackageManager.NameNotFoundException) {
        false  // App is not installed
    }
}

fun Context.openApp(packageName: String) {
    if (isAppInstalled(packageName)) {
        Log.d("MyTag","Entered if")
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            this.startActivity(intent)
        } else {
            val playStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            this.startActivity(playStoreIntent)
        }
    } else {
        Log.d("MyTag","Entered else")
        val playStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
        this.startActivity(playStoreIntent)
    }
}


@SuppressLint("QueryPermissionsNeeded")
fun Context.openGoogleMap(latitude: Double, longitude: Double){
    // Construct the URL for the Google Maps location
    val geoUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")

    // Create an intent to view the location in Google Maps
    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
    mapIntent.setPackage("com.google.android.apps.maps") // Open in Google Maps app

    // Check if the Google Maps app is available, and open the intent
    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    } else {
        // Optionally, fall back to opening the location in a browser if Google Maps is not installed
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$latitude,$longitude"))
        startActivity(browserIntent)
    }
}

fun NavHostController.navigateSingleTopTo(
    route: NavigationScreens,
    navHostController: NavHostController
) = this.navigate(route) {
    // If the destination doesn't exist, pop up to the start destination
    popUpTo(navHostController.graph.findStartDestination().id) {
        //inclusive = true this means in the backstack entry the old destination will be replaced by the new destination
        //inclusive = false this means the backstack entry will put the new destination at the top and below it will keep the old destination
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}


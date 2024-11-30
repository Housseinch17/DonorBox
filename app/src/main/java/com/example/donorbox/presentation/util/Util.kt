package com.example.donorbox.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
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


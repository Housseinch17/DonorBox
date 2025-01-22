package com.example.donorbox.presentation.screens.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FabPosition
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.navigation.navGraphBuilder.donorBoxGraph
import com.example.donorbox.presentation.navigation.navGraphBuilder.registerGraph
import com.example.donorbox.presentation.screens.authentication.AuthenticationViewModel
import com.example.donorbox.presentation.screens.authentication.SignOutResponse
import com.example.donorbox.presentation.theme.ClickedIconColor
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.ShimmerEffect
import com.example.donorbox.presentation.util.navigateSingleTopTo
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainPage(navController: NavHostController, startDestination: NavigationScreens) {
    Log.d("MyTag", "Entered")
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination
    val currentGraph = currentDestination?.parent

    val currentGraphRoute = checkCurrentGraphRoute(currentGraph?.route.toString())

    val context = LocalContext.current

    val authenticationViewModel = koinViewModel<AuthenticationViewModel>()
    val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()

    val bottomBarLoading: Boolean = authenticationUiState.bottomBarLoading

    LaunchedEffect(authenticationUiState.signOut) {
        when (authenticationUiState.signOut) {
            SignOutResponse.Success -> {
                authenticationViewModel.resetSignOutState()
                navController.navigate(NavigationScreens.LogInPage) {
                    //popUpTo(0) here 0 means we will remove all the old stacks in BackStackEntry
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }

    LaunchedEffect(authenticationViewModel.eventMessage) {
        authenticationViewModel.eventMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    androidx.compose.material.Scaffold(
        floatingActionButton = {
            if (currentGraphRoute != NavigationScreens.RegisterGraph.ROUTE && !bottomBarLoading) {
                FloatingButtonBar(
                    currentScreens = authenticationUiState.currentScreen,
                    onDonationClick = {
                        navController.navigateSingleTopTo(
                            route = NavigationScreens.ReceivedDonationsPage,
                            navController
                        )
                        authenticationViewModel.updateCurrentScreen(NavigationScreens.ReceivedDonationsPage)
                    },
                )
            }
        },
        //to show it docked
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (currentGraphRoute != NavigationScreens.RegisterGraph.ROUTE) {
                if (bottomBarLoading) {
                    ShimmerEffect(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                } else {
                    androidx.compose.material.BottomAppBar(
                        backgroundColor = NewBlue,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(56.dp),
                        //will cut floating to show white background behind it
                        cutoutShape = RoundedCornerShape(50),
                        content = {
                            BottomAppBar(
                                currentScreens = authenticationUiState.currentScreen,
                                onHomeClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.HomePage,
                                        navController
                                    )
                                    authenticationViewModel.updateCurrentScreen(NavigationScreens.HomePage)
                                },
                                onMyDonationsClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.MyDonationsPage,
                                        navController
                                    )
                                    authenticationViewModel.updateCurrentScreen(NavigationScreens.MyDonationsPage)
                                },
                                onSettingsClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.SettingsPage,
                                        navController
                                    )
                                    authenticationViewModel.updateCurrentScreen(NavigationScreens.SettingsPage)
                                },
                                onProfileClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.ProfilePage,
                                        navController
                                    )
                                    authenticationViewModel.updateCurrentScreen(NavigationScreens.ProfilePage)
                                }
                            )
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        SharedScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp),
                navController = navController,
                startDestination = startDestination
            ) {
                registerGraph(
                    authenticationViewModel = authenticationViewModel,
                    authenticationUiState = authenticationUiState,
                    navHostController = navController,
                )

                donorBoxGraph(
                    navHostController = navController,
                    authenticationUiState = authenticationUiState,
                    authenticationViewModel = authenticationViewModel,
                )
            }
        }
    }
}

@Composable
fun FloatingButtonBar(onDonationClick: () -> Unit, currentScreens: NavigationScreens) {
    FloatingActionButton(
        modifier = Modifier.padding(top = 10.dp),
        onClick = onDonationClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = NewBlue,
        contentColor = if (currentScreens == NavigationScreens.ReceivedDonationsPage) ClickedIconColor else NewWhite
    ) {
        Icon(
            imageVector = Icons.Filled.Outbox,
            contentDescription = "Received Donations",
        )
    }
}

//BottomBar without Floating Button
@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit, onProfileClick: () -> Unit,
    onMyDonationsClick: () -> Unit, onSettingsClick: () -> Unit,
    currentScreens: NavigationScreens,
) {
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = NewBlue,
    ) {
        BottomNavigationItem(
            selected = currentScreens == NavigationScreens.HomePage,
            selectedContentColor = NewWhite,
            unselectedContentColor = ClickedIconColor,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (currentScreens == NavigationScreens.HomePage) ClickedIconColor else NewWhite
                )
            },
        )
        BottomNavigationItem(
            selected = currentScreens == NavigationScreens.ProfilePage,
            unselectedContentColor = NewWhite,
            selectedContentColor = ClickedIconColor,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = if (currentScreens == NavigationScreens.ProfilePage) ClickedIconColor else NewWhite
                )
            }
        )

        BottomNavigationItem(
            selected = currentScreens == NavigationScreens.MyDonationsPage,
            unselectedContentColor = NewWhite,
            selectedContentColor = ClickedIconColor,
            onClick = onMyDonationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.ShoppingBag,
                    contentDescription = "My Donations",
                    tint = if (currentScreens == NavigationScreens.MyDonationsPage) ClickedIconColor else NewWhite
                )
            }
        )
        BottomNavigationItem(
            selected = currentScreens == NavigationScreens.SettingsPage,
            unselectedContentColor = NewWhite,
            selectedContentColor = ClickedIconColor,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = if (currentScreens == NavigationScreens.SettingsPage) ClickedIconColor else NewWhite
                )
            }
        )
    }
}

private fun checkCurrentGraphRoute(route: String): String {
    return route.substringAfterLast(".")
}

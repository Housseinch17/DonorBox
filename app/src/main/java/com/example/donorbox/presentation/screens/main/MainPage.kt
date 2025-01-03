package com.example.donorbox.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.donorbox.presentation.navigation.Navigation
import com.example.donorbox.presentation.navigation.NavigationScreens
import com.example.donorbox.presentation.theme.MatteBlack
import com.example.donorbox.presentation.theme.Orange
import com.example.donorbox.presentation.util.navigateSingleTopTo
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainPage(navController: NavHostController) {
    //initialize viewmodel to check if already logged in via shared preferences
    val mainViewModel = koinViewModel<MainViewModel>()
    val status by mainViewModel.status.collectAsStateWithLifecycle()


    val backStackEntry by navController.currentBackStackEntryAsState()
    //get current screen
    val currentScreenDestination = backStackEntry?.destination?.route
    //get current screen route without (com.example.donorBox...)
    val currentScreenRoute = getScreenName(currentScreenDestination)

    //not hideBottomBar
    val showBottomBar = !(hideBottomBar(currentScreenRoute)) && !status.isLoading

    //here if the status is NavigationScreens.Loading it will show the background image set in the theme
    if (status.currentScreen != NavigationScreens.Loading) {
        //use material not material 3 to use docked
        androidx.compose.material.Scaffold(
            floatingActionButton = {
                if (showBottomBar) {
                    FloatingButtonBar(onDonationClick = {
                            navController.navigateSingleTopTo(
                                route = NavigationScreens.ReceivedDonationsPage,
                                navController
                            )
                    })
                }
            },
            //to show it docked
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                if (showBottomBar) {
                    androidx.compose.material.BottomAppBar(
                        backgroundColor = MatteBlack,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(56.dp),
                        //will cut floating to show white background behind it
                        cutoutShape = RoundedCornerShape(50),
                        content = {
                            BottomAppBar(
                                onHomeClick = {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.HomePage,
                                            navController
                                        )
                                },
                                onMyDonationsClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.MyDonationsPage,
                                        navController
                                    )
                                },
                                onSettingsClick = {
                                    navController.navigateSingleTopTo(
                                        route = NavigationScreens.SettingsPage,
                                        navController
                                    )
                                },
                                onProfileClick = {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.ProfilePage,
                                            navController
                                        )
                                }
                            )
                        }
                    )
                }

            },
        ) { innerPadding ->
            //we  use innerPadding because we added statusBarsPadding() and navigationBarsPadding()
            // to topBar and bottomBar
            //but here i removed topBar so i had to add statusBarsPadding() to show status bar color
            Navigation(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(innerPadding),
                navHostController = navController,
                startDestination = status.currentScreen
            )
        }
    }
}

@Composable
fun FloatingButtonBar(onDonationClick: () -> Unit) {
    FloatingActionButton(
        onClick = onDonationClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = Color.Green
    ) {
        Icon(
            imageVector = Icons.Filled.Outbox,
            contentDescription = "Received Donations",
            tint = Color.White
        )
    }
}

//BottomBar without Floating Button
@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit, onProfileClick: () -> Unit,
    onMyDonationsClick: () -> Unit, onSettingsClick: () -> Unit
) {
    BottomNavigation(
        backgroundColor = MatteBlack,
    ) {
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = true,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            }
        )
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            }
        )

        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onMyDonationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.ShoppingBag,
                    contentDescription = "My Orders",
                    tint = Color.White
                )
            }
        )
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        )
    }
}


private fun hideBottomBar(currentScreenRoute: String): Boolean {
    //currentScreenRoute is LogInPage
    //currentScreenRoute is SignUpPage
    //currentScreenRoute is Empty
    return currentScreenRoute == NavigationScreens.LogInPage.ROUTE || currentScreenRoute == NavigationScreens.SignUpPage.ROUTE || currentScreenRoute.isEmpty()
}

//instead of getting com.example.donorBox.LogInPage it will show LogInPage only
private fun getScreenName(route: String?): String {
    return route?.substringAfterLast('.') ?: ""
}
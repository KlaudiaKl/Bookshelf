package com.klaudia.bookshelf.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.klaudia.bookshelf.navigation.Screen

@Composable
fun AppBottomNavigation(navHostController: NavHostController) {
    val items = listOf(
        Screen.HomeScreen,
        Screen.SavedVolumesScreen
    )
    NavigationBar {
        val currentRoute = navHostController.currentDestination?.route
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navHostController.navigate(screen.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.HomeScreen -> Icons.Default.Home
                            Screen.SavedVolumesScreen -> Icons.Default.Favorite
                            else -> Icons.Default.Home // Fallback icon
                        },
                        contentDescription = "Navigation Icon"
                    )
                }
                )

        }
    }
}
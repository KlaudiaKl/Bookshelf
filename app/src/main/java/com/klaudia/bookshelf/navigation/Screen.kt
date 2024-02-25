package com.klaudia.bookshelf.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen(route = "home_screen")
}
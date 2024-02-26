package com.klaudia.bookshelf.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen(route = "home_screen")
    object SearchResultsScreen: Screen(route = "search_results_screen/{query}"){
        fun createRoute(query:String) = "search_results_screen/$query"
    }
}
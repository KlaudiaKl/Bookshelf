package com.klaudia.bookshelf.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.screens.home.HomeScreen
import com.klaudia.bookshelf.presentation.screens.home.HomeViewModel
import com.klaudia.bookshelf.presentation.screens.search.SearchResultsScreen
import com.klaudia.bookshelf.presentation.screens.search.SearchViewModel

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestination) {
        homeScreenRoute(
            navigateToSearchResultsScreen = {
                navController.navigate(
                    Screen.SearchResultsScreen.createRoute(
                        it
                    )
                )
            }
        )
        searchResultsRoute()
    }
}

fun NavGraphBuilder.homeScreenRoute(
    navigateToSearchResultsScreen: (String) -> Unit
) {
    composable(Screen.HomeScreen.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val newestVolumes by viewModel.newestBooks.collectAsState()
        val oopBooks by viewModel.oopBooks.collectAsState()
        val kotlinBooks by viewModel.kotlinBooks.collectAsState()
        val composeBooks by viewModel.composeBooks.collectAsState()
        var items = emptyList<VolumeItem>()
        var oopItems = emptyList<VolumeItem>()
        var kotlinItems = emptyList<VolumeItem>()
        var composeItems = emptyList<VolumeItem>()

        when (newestVolumes) {
            is RequestState.Success -> {
                val successState = newestVolumes as RequestState.Success<VolumeApiResponse?>
                if (successState.data != null)
                    items = successState.data.items
            }
            is RequestState.Loading -> {
                CircularProgressIndicator()
            }
            is RequestState.Error -> {
                Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
            }
        }

        when (oopBooks) {
            is RequestState.Success -> {
                oopItems = (oopBooks as RequestState.Success<List<VolumeItem>>).data
            }
            is RequestState.Loading -> {
                CircularProgressIndicator()
            }
            is RequestState.Error -> {
                Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
            }
        }
        when (kotlinBooks) {
            is RequestState.Success -> {
                kotlinItems = (kotlinBooks as RequestState.Success<List<VolumeItem>>).data
            }
            is RequestState.Loading -> {
                CircularProgressIndicator()
            }
            is RequestState.Error -> {
                Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
            }
        }
        when (composeBooks) {
            is RequestState.Success -> {
                composeItems = (composeBooks as RequestState.Success<List<VolumeItem>>).data
            }
            is RequestState.Loading -> {
                CircularProgressIndicator()
            }
            is RequestState.Error -> {
                Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
            }
        }
        Log.d("List", items.toString())

        HomeScreen(
            volumes = items,
            oopItems = oopItems,
            kotlinItems = kotlinItems,
            composeItems = composeItems,
            onButtonClick = {
                navigateToSearchResultsScreen(it)
            }
        )
    }
}

fun NavGraphBuilder.searchResultsRoute() {
    composable(
        route = Screen.SearchResultsScreen.route,
        arguments = listOf(navArgument("query") { type = NavType.StringType })
    ) {
        val arg = it.arguments?.getString("query") ?: ""
        val viewModel: SearchViewModel = hiltViewModel()
        LaunchedEffect(arg) {
            viewModel.search(arg, false)
        }
        val searchResults by viewModel.searchResults.collectAsState()

        var items: List<VolumeItem>
        when (searchResults) {
            is RequestState.Success -> {
                val successState = (searchResults as RequestState.Success<List<VolumeItem>>).data
                items = successState
                SearchResultsScreen(
                    result = items, query = arg, viewModel = viewModel
                )
            }

            is RequestState.Loading -> {
                CircularProgressIndicator()
            }

            is RequestState.Error -> {
                Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
                Text(text = "Error")
            }
        }
    }
}
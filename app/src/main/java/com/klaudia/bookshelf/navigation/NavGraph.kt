package com.klaudia.bookshelf.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
        val newestVolumes = viewModel.newestBooks.collectAsState().value
        var items = emptyList<VolumeItem>()
        var query by remember { mutableStateOf("") }

        when (newestVolumes) {
            is RequestState.Success -> {
                if (newestVolumes.data != null)
                    items = newestVolumes.data.items
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
            onButtonClick = {
                navigateToSearchResultsScreen(it)
            }
        )
    }
}

fun NavGraphBuilder.searchResultsRoute() {
    composable(
        route = Screen.SearchResultsScreen.route,
        arguments = listOf(navArgument("query"){type = NavType.StringType})
    ) {
        val arg = it.arguments?.getString("query") ?: ""
        val viewModel: SearchViewModel = hiltViewModel()
       LaunchedEffect(arg){
            viewModel.search(arg)
        }
        val searchResults by viewModel.searchResults.collectAsState()
        var items = emptyList<VolumeItem>()
        when (searchResults) {
            is RequestState.Success -> {
                val successState = searchResults as RequestState.Success<VolumeApiResponse?>
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

        SearchResultsScreen(result = items, query = arg)

    }
}
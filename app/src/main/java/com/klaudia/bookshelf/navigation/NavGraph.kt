package com.klaudia.bookshelf.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.screens.home.HomeScreen
import com.klaudia.bookshelf.presentation.screens.home.HomeViewModel

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestination) {
        homeScreenRoute(

        )
    }
}

fun NavGraphBuilder.homeScreenRoute() {
    composable(Screen.HomeScreen.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val newestVolumes = viewModel.newestBooks.collectAsState().value
        var items = emptyList<VolumeItem>()

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

        HomeScreen(volumes = items)
    }
}
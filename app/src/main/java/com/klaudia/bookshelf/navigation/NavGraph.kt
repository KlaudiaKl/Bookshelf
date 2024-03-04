package com.klaudia.bookshelf.navigation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.AppBottomNavigation
import com.klaudia.bookshelf.presentation.components.EmptyScreen
import com.klaudia.bookshelf.presentation.screens.book_details.DetailsScreen
import com.klaudia.bookshelf.presentation.screens.book_details.DetailsViewModel
import com.klaudia.bookshelf.presentation.screens.home.CombinedBooksState
import com.klaudia.bookshelf.presentation.screens.home.HomeScreen
import com.klaudia.bookshelf.presentation.screens.home.HomeViewModel
import com.klaudia.bookshelf.presentation.screens.saved_volumes.SavedVolumesScreen
import com.klaudia.bookshelf.presentation.screens.saved_volumes.SavedVolumesViewModel
import com.klaudia.bookshelf.presentation.screens.search.SearchResultsScreen
import com.klaudia.bookshelf.presentation.screens.search.SearchViewModel

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(bottomBar = {
        if (currentRoute == Screen.HomeScreen.route || currentRoute == Screen.SavedVolumesScreen.route) AppBottomNavigation(navHostController = navController)}
    ) { padding ->


        NavHost(navController = navController, startDestination = startDestination, modifier = Modifier.padding(padding)) {
            homeScreenRoute(
                navigateToSearchResultsScreen = {
                    navController.navigate(
                        Screen.SearchResultsScreen.createRoute(
                            it
                        )
                    )
                },
                navigateToDetailsScreen = {
                    navController.navigate(
                        Screen.DetailsScreen.createDetailsRoute(
                            it
                        )
                    )
                }
            )
            searchResultsRoute(
                navigateToDetailsScreen = {
                    navController.navigate(
                        Screen.DetailsScreen.createDetailsRoute(it)
                    )
                }
            )
            detailScreenRoute()
            savedVolumesRoute(
                navigateToDetailsScreen = {
                    navController.navigate(
                        Screen.DetailsScreen.createDetailsRoute(it)
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.homeScreenRoute(
    navigateToSearchResultsScreen: (String) -> Unit,
    navigateToDetailsScreen: (String) -> Unit
) {
    composable(Screen.HomeScreen.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val state by viewModel.combinedBooksState.collectAsState(initial = CombinedBooksState(isLoading = true))

        when {
            state.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.error != null -> {
                EmptyScreen()
            }
            else -> HomeScreen(
                volumes = state.newestVolumes ?: emptyList(),
                oopItems = state.oopBooks ?: emptyList(),
                kotlinItems = state.kotlinBooks ?: emptyList(),
                composeItems = state.composeBooks ?: emptyList(),
                onButtonClick = navigateToSearchResultsScreen,
                onVolumeClick = navigateToDetailsScreen,
                onSeeMoreButtonClick = navigateToSearchResultsScreen
            )
        }
    }
}

fun NavGraphBuilder.searchResultsRoute(
    navigateToDetailsScreen: (String) -> Unit
) {
    composable(
        route = Screen.SearchResultsScreen.route,
        arguments = listOf(navArgument("query") { type = NavType.StringType })
    ) {
        val arg = it.arguments?.getString("query") ?: ""
        val viewModel: SearchViewModel = hiltViewModel()
        LaunchedEffect(arg) {
            viewModel.search(arg, false, filter = null)
        }
        val searchResults by viewModel.searchResults.collectAsState()

        val items: List<VolumeItem>
        when (searchResults) {
            is RequestState.Success -> {
                val successState = (searchResults as RequestState.Success<List<VolumeItem>>).data
                items = successState
                SearchResultsScreen(
                    result = items, query = arg, viewModel = viewModel, onVolumeClick = {
                        navigateToDetailsScreen(it)
                    }
                )
            }

            is RequestState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RequestState.Error -> {
                // Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "newestVolumes error")
                Text(text = "Error")
            }
        }
    }
}

fun NavGraphBuilder.detailScreenRoute() {
    composable(
        route = Screen.DetailsScreen.route,
        arguments = listOf(navArgument("volumeId") { type = NavType.StringType })
    ) {
        val volumeId = it.arguments?.getString("volumeId") ?: ""
        val context = LocalContext.current
        val viewModel: DetailsViewModel = hiltViewModel()
        LaunchedEffect(volumeId) {
            viewModel.getVolume(volumeId)
        }
        val isVolumeSaved by viewModel.isVolumeSaved(volumeId).collectAsState(initial = false)
        when (val volume = viewModel.volumeItem.collectAsState().value) {
            is RequestState.Success -> {
                DetailsScreen(
                    volume = volume.data,
                    onHyperLinkClick = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    isVolumeSaved = isVolumeSaved,
                    onSaveClicked = {
                        val toBeSavedVolume = SavedVolume(
                            title = volume.data.volumeInfo.title,
                            authors = volume.data.volumeInfo.authors ?: emptyList(),
                            id = volume.data.id,
                            thumbnailUrl = volume.data.volumeInfo.imageLinks?.thumbnail ?: "",
                            description = volume.data.volumeInfo.description ?: "",
                            pageCount = volume.data.volumeInfo.pageCount,
                            language = volume.data.volumeInfo.language,
                            publishingDate = volume.data.volumeInfo.publishedDate,
                            link = volume.data.saleInfo.buyLink ?: ""
                        )
                        if(!isVolumeSaved){
                            viewModel.addVolumeToSaved(toBeSavedVolume)
                        }
                        else{
                            viewModel.deleteVolumeFromSaved(volumeId)
                        }

                    }
                )
            }

            is RequestState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RequestState.Error -> {
                volume.exception.message?.let { it1 -> Log.d("DetailsScreenError", it1) }
                EmptyScreen()
            }
        }
    }
}

fun NavGraphBuilder.savedVolumesRoute(
    navigateToDetailsScreen: (String) -> Unit
) {
    composable(
        route = Screen.SavedVolumesScreen.route
    ) {
        val viewModel: SavedVolumesViewModel = hiltViewModel()
        val books = viewModel.volumes.collectAsState().value
        when (books) {
            is RequestState.Success -> {


                SavedVolumesScreen(result = books.data, onVolumeClick = {
                    navigateToDetailsScreen(it)
                })
            }

            is RequestState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RequestState.Error -> {
                // Toast.makeText(LocalContext.current, "Error occurred", Toast.LENGTH_SHORT).show()
                Log.d("Error (navGraph)", "savedVolumes error")
                EmptyScreen()
            }
        }

    }
}
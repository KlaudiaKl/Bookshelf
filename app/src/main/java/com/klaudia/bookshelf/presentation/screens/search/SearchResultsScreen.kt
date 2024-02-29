package com.klaudia.bookshelf.presentation.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.HorizontalVolumeItemHolder
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@Composable
fun SearchResultsScreen(
    result: List<VolumeItem>,
    query: String,
    viewModel : SearchViewModel,
    onVolumeClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    Column {
        Text(text = "Search results for: $query")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(state = listState) {

            items(items = result, key = { item -> item.id }) {
                if (!it.volumeInfo.title.isNullOrEmpty()) {
                    HorizontalVolumeItemHolder(
                        imageUrl = it.volumeInfo.imageLinks?.thumbnail?:"",
                        title = it.volumeInfo.title,
                        onClick = {onVolumeClick(it.id)}

                    )
                    Log.d(
                        "HorizontalVolumeItemHolder",
                        "Loading item with title: ${it.volumeInfo.title}"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(listState){
        snapshotFlow {
            // Trigger earlier than the last item, e.g., when reaching the last 3 items
            listState.layoutInfo.visibleItemsInfo.any { it.index >= listState.layoutInfo.totalItemsCount - 3 }
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                Log.d("LazyLoading", "Triggering load more items")
                coroutineScope.launch {
                    viewModel.search(query, true)
                }
            }

    }
}
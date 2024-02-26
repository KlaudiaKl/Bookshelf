package com.klaudia.bookshelf.presentation.screens.search

import android.app.appsearch.SearchResults
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.HorizontalVolumeItemHolder


@Composable
fun SearchResultsScreen(
    result: List<VolumeItem>,
    query: String
) {
    Column {


        Text(text = "Search results for: $query")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn() {

            items(items = result, key = {item -> item.id}) {

                HorizontalVolumeItemHolder(
                    imageUrl = it.volumeInfo.imageLinks.smallThumbnail,
                    title = it.volumeInfo.title

                )
                Log.d("HorizontalVolumeItemHolder", "Loading item with title: ${it.volumeInfo.title}")
                Spacer(modifier = Modifier.height(4.dp))

            }

        }

    }
}
package com.klaudia.bookshelf.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.VolumeItemHolder


@Composable
fun HomeScreen(
    volumes: List<VolumeItem>
) {

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bookshelf", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow() {

            items(items = volumes) {
                VolumeItemHolder(imageUrl = it.volumeInfo.imageLinks.smallThumbnail, title = it.volumeInfo.title)
                Spacer(modifier = Modifier.width(4.dp))
                //Text(text = it.volumeInfo.title)
            }

        }
    }

}


package com.klaudia.bookshelf.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.VolumeItemHolder


@Composable
fun HomeScreen(
    volumes: List<VolumeItem>,
    oopItems: List<VolumeItem>,
    kotlinItems: List<VolumeItem>,
    composeItems: List<VolumeItem>,
    onButtonClick: (String) -> Unit,
    onVolumeClick: (String) -> Unit
    //onQueryChange: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var showButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(query) {
        showButton = query.isNotEmpty()
    }
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState()) ) {
        Text(text = "Bookshelf", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {

                query = it
            },
            label = { Text(text = "Search") },
            singleLine = true,
            modifier = Modifier.padding(8.dp)
        )
        if (showButton) {
            Button(
                onClick = { onButtonClick(query) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Search")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Newest Android Books", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {

            items(items = volumes,key = { item -> item.id}) {
                if (it.volumeInfo.title.isNotEmpty()) {
                    VolumeItemHolder(
                        imageUrl = it.volumeInfo.imageLinks?.thumbnail?:"",
                        title = it.volumeInfo.title,
                        onClick = {onVolumeClick(it.id)}
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Object Oriented Programming", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {

            items(items = oopItems,key = { item -> item.id}) {
                if (it.volumeInfo.title.isNotEmpty()) {
                    VolumeItemHolder(
                        imageUrl = it.volumeInfo.imageLinks?.thumbnail?:"",
                        title = it.volumeInfo.title,
                        onClick = {onVolumeClick(it.id)}
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Learn Kotlin", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {

            items(items = kotlinItems,key = { item -> item.id}) {
                if (it.volumeInfo.title.isNotEmpty()) {
                    VolumeItemHolder(
                        imageUrl = it.volumeInfo.imageLinks?.thumbnail?:"",
                        title = it.volumeInfo.title,
                        onClick = {onVolumeClick(it.id)}
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "About Jetpack Compose", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {

            items(items = composeItems,key = { item -> item.id}) {
                if (it.volumeInfo.title.isNotEmpty()) {
                    VolumeItemHolder(
                        imageUrl = it.volumeInfo.imageLinks?.thumbnail?:"",
                        title = it.volumeInfo.title,
                        onClick = {onVolumeClick(it.id)}
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                }
            }

        }
    }
}



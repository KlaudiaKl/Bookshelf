package com.klaudia.bookshelf.presentation.screens.saved_volumes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.presentation.components.HorizontalVolumeItemHolder

@Composable
fun SavedVolumesScreen(
    result: List<SavedVolume>,
    onVolumeClick : (String) -> Unit
) {

    Column(modifier = Modifier.padding(16.dp)) {
        if (result.isNullOrEmpty()) {
            Text(text = "You have not saved any books yet.", style = MaterialTheme.typography.titleLarge)
            Text(text = "Click on a book a tap the Save button to add one.", style = MaterialTheme.typography.titleMedium)
        }
        else {
            Text(text = "Books you've saved:", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn() {

                items(items = result, key = { item -> item.id }) {
                    if (!it.title.isNullOrEmpty()) {
                        HorizontalVolumeItemHolder(
                            imageUrl = it.thumbnailUrl ?: "",
                            title = it.title,
                            onClick = { onVolumeClick(it.id) }

                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }

}
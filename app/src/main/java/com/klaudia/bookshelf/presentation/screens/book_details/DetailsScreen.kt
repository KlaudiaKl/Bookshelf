package com.klaudia.bookshelf.presentation.screens.book_details


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.components.DisplayImageFromUrl
import com.klaudia.bookshelf.presentation.components.HyperlinkText

@Composable
fun DetailsScreen(
    volume: VolumeItem,
    onHyperLinkClick: (String) -> Unit,
    onSaveClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DisplayImageFromUrl(
                imageUrl = volume.volumeInfo.imageLinks?.thumbnail ?: "",
                contentDescription = "Thumbnail image",
                modifier = Modifier.width(150.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = volume.volumeInfo.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Button(onClick = { onSaveClicked() }) {
            Row {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "saved icon" )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Save")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!volume.volumeInfo.authors.isNullOrEmpty()) Text(text = "By ${volume.volumeInfo.authors.joinToString(separator = ", ") }")
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 4.dp
        ) {
            Column {
                DetailItem(text = "Language: ${volume.volumeInfo.language}")
                DetailItem(text = "Published: ${volume.volumeInfo.publishedDate}")
                DetailItem(text = "Pages: ${volume.volumeInfo.pageCount}")
                if (volume.saleInfo.saleability == "FREE"){
                    DetailItem(text = "This book is free")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (!volume.volumeInfo.description.isNullOrEmpty()) {
            val formattedDescription = HtmlCompat.fromHtml(
                volume.volumeInfo.description,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 4.dp
            ) {
                Text(text = formattedDescription.toString(), modifier = Modifier.padding(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(!volume.saleInfo.buyLink.isNullOrEmpty()){
            HyperlinkText(text = "Click here to get this book", url = volume.saleInfo.buyLink, onClick ={ onHyperLinkClick(it)})
        }


    }
}

@Composable
fun DetailItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Rounded.Check, contentDescription = "Bullet point")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@Preview
@Composable
fun DetailsScreenPrev() {

}
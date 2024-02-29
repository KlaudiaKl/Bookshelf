package com.klaudia.bookshelf.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VolumeItemHolder(
    imageUrl: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(180.dp)
            .clickable { onClick() }, verticalArrangement = Arrangement.Top
    ){
        DisplayImageFromUrl(imageUrl = imageUrl, contentDescription = "Volume cover image", modifier = Modifier.fillMaxWidth().height(100.dp))
        //Spacer(modifier = Modifier.height(4.dp))
        Text(text = title,
            //modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall)
            //fontStyle = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
fun VolumeItemHolderPreview() {
    VolumeItemHolder(
        imageUrl = "https://books.google.com/books?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
        title = "The Google story",
        onClick = {

        })
}
package com.klaudia.bookshelf.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalVolumeItemHolder(imageUrl: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically

    ){
        DisplayImageFromUrl(imageUrl = imageUrl, contentDescription = "Volume cover image", modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = title?:"Untitled",
            //modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall)
        //fontStyle = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
fun HorizontalItemPrev() {

        HorizontalVolumeItemHolder(
            imageUrl = "https://books.google.com/books?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
            title = "The Google story")

    
}
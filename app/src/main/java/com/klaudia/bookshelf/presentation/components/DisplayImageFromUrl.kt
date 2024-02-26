package com.klaudia.bookshelf.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.klaudia.bookshelf.R

@Composable
fun DisplayImageFromUrl(imageUrl: String, contentDescription: String, modifier : Modifier) {
 AsyncImage(
     model = ImageRequest.Builder(LocalContext.current)
         .data(imageUrl)
         .crossfade(true)
         .build(),
     contentDescription = contentDescription,
     placeholder = painterResource(id = R.drawable.image),
     error = painterResource(id = R.drawable.image),
     modifier = modifier,
     contentScale = ContentScale.Crop, onError = { it.result.throwable.message?.let { it1 ->
         Log.d("image loading error",
             it1
         )
     } }
 )
}
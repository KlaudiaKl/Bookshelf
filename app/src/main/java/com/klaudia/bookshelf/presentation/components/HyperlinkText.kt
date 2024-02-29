package com.klaudia.bookshelf.presentation.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HyperlinkText(text: String, url: String, onClick: (String) -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(Color.Blue)){
            append(text)
        }
        addStringAnnotation(tag = "URL", annotation = url, start = 0, end = text.length)
    }

    ClickableText(text = annotatedString, onClick = {
        offset ->
        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
            .firstOrNull()?.let {
                annotation ->
                onClick(annotation.item)
            }
    })
}
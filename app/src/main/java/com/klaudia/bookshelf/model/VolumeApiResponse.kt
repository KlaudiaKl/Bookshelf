package com.klaudia.bookshelf.model

data class VolumeApiResponse (
    val kind: String,
    val totalItems: Int,
    val items: List<VolumeItem>
)
package com.klaudia.bookshelf.db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "saved_volumes")
data class SavedVolume(
    @PrimaryKey val id: String,
    val title: String,
    val authors: List<String>,
    val thumbnailUrl: String,
    val description: String,
    val pageCount: Int,
    val language: String,
    val publishingDate: String,
    val link: String

)

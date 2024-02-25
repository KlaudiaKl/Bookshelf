package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import retrofit2.Call
import retrofit2.Response

interface BooksRepository {
    suspend fun searchBooks(query:String)
    suspend fun listVolumesFromNewest(query: String) : VolumeApiResponse?
}
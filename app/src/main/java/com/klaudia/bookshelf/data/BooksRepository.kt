package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.model.VolumeApiResponse


interface BooksRepository {
    suspend fun searchBooks(query:String, startIndex: Int): RequestState<VolumeApiResponse>?
    suspend fun listVolumesFromNewest(query: String) : VolumeApiResponse?

    suspend fun listVolumesByCategory(category: String): VolumeApiResponse?
}
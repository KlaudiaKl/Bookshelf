package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem


interface BooksRepository {
    suspend fun searchBooks(query:String, startIndex: Int): RequestState<VolumeApiResponse>?
    suspend fun getVolumeById(id: String): RequestState<VolumeItem>
    suspend fun listVolumesFromNewest(query: String) : VolumeApiResponse?

    suspend fun listVolumesByCategory(category: String): VolumeApiResponse?

    suspend fun insertVolumeToSaved(volume: SavedVolume)
    suspend fun getAllSavedVolumes(): List<SavedVolume>
}
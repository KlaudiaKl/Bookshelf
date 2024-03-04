package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import kotlinx.coroutines.flow.Flow


interface BooksRepository {
    suspend fun searchBooks(query:String, startIndex: Int, filter: String?): RequestState<VolumeApiResponse>?
    suspend fun getVolumeById(id: String): RequestState<VolumeItem>
    suspend fun listVolumesFromNewest(query: String) : VolumeApiResponse?

    suspend fun listVolumesByCategory(category: String): VolumeApiResponse?

    suspend fun insertVolumeToSaved(volume: SavedVolume)
     fun getAllSavedVolumes(): Flow<List<SavedVolume>>
    suspend fun deleteVolumeFromSaved(volumeId: String)

    fun isVolumeSaved(volumeId: String): Flow<Boolean>
}
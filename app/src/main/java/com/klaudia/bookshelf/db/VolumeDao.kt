package com.klaudia.bookshelf.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VolumeDao {

    @Query("SELECT * FROM saved_volumes")
    fun getAllSavedVolumes(): Flow<List<SavedVolume>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(volume: SavedVolume)

    @Query("DELETE FROM saved_volumes WHERE id = :volumeId")
    suspend fun deleteById(volumeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_volumes WHERE id = :volumeId)")
    fun isVolumeSaved(volumeId: String): Flow<Boolean>

}
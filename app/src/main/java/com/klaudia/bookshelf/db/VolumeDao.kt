package com.klaudia.bookshelf.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VolumeDao {

    @Query("SELECT * FROM saved_volumes")
    suspend fun getAllSavedVolumes(): List<SavedVolume>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(volume: SavedVolume)

    @Delete
    suspend fun delete(volume: SavedVolume)

}
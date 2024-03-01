package com.klaudia.bookshelf.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klaudia.bookshelf.util.Converters

@Database(entities = [SavedVolume::class], version = 1)
@TypeConverters(Converters::class)
abstract class SavedVolumeDatabase : RoomDatabase() {
    abstract fun volumeDao(): VolumeDao

}
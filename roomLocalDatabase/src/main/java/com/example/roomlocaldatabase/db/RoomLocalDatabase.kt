package com.example.roomlocaldatabase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomlocaldatabase.dao.MetadataDAO
import com.example.roomlocaldatabase.entity.MetadataEntity

@Database(
    version = 1,
    entities = [MetadataEntity::class]
)
abstract class RoomLocalDatabase: RoomDatabase() {

    abstract fun metadataDAO(): MetadataDAO
}
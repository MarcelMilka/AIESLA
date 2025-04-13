package com.example.roomlocaldatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomlocaldatabase.db.RoomLocalDatabaseConstants

@Entity(tableName = RoomLocalDatabaseConstants.METADATA)
data class MetadataEntity(
    @PrimaryKey val index: Int,
    val relatedUUID: String?,
    val signedIn: Boolean
)
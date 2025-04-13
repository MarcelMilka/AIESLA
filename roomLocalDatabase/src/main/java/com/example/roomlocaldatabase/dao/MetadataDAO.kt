package com.example.roomlocaldatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roomlocaldatabase.entity.MetadataEntity

@Dao
interface MetadataDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun initializeMetadata(metadataEntity: MetadataEntity)

    @Query("SELECT * FROM Metadata WHERE `index` = 0")
    suspend fun getMetadata(): MetadataEntity?

    @Query("UPDATE Metadata SET relatedUUID = :uuid WHERE `index` = 0")
    suspend fun assignRelatedUUID(uuid: String)

    @Query("SELECT signedIn FROM Metadata WHERE `index` = 0")
    fun isSignedIn(): Boolean

    @Query("UPDATE Metadata SET signedIn = true WHERE `index` = 0")
    suspend fun signIn()

    @Query("UPDATE Metadata SET signedIn = false WHERE `index` = 0")
    suspend fun signOut()
}
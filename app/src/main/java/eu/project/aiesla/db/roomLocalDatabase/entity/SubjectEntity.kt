package eu.project.aiesla.db.roomLocalDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.project.aiesla.db.roomLocalDatabase.db.RoomLocalDatabaseConstants

@Entity(tableName = RoomLocalDatabaseConstants.SUBJECTS)
data class SubjectEntity(
    val nameOfSubject: String,
    @PrimaryKey val idOfSubject: String,
    val idsOfContainedNotebooks: String,
    val idsOfContainedSets: String
)
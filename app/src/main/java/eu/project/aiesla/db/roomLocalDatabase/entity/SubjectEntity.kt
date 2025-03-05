package eu.project.aiesla.db.roomLocalDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Entities")
data class SubjectEntity(
    val nameOfSubject: String,
    @PrimaryKey val idOfSubject: String,
    val idsOfContainedNotebooks: String
)
package eu.project.aiesla.db.roomLocalDatabase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.project.aiesla.db.roomLocalDatabase.dao.SubjectDAO
import eu.project.aiesla.db.roomLocalDatabase.entity.SubjectEntity

@Database(
    version = 0,
    entities = [SubjectEntity::class]
)
abstract class RoomLocalDatabase: RoomDatabase() {

    abstract fun subjectDAO(): SubjectDAO
}
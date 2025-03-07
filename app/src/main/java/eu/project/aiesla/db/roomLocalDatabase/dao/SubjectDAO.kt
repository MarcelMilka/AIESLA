package eu.project.aiesla.db.roomLocalDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import eu.project.aiesla.db.roomLocalDatabase.entity.SubjectEntity

@Dao
interface SubjectDAO {

    @Query("SELECT * FROM Subjects")
    suspend fun getAllSubjects(): List<SubjectEntity>
}
package eu.project.aiesla.core.routeSignedIn.routeStudyScreen.data

import eu.project.aiesla.db.roomLocalDatabase.dao.SubjectDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyRepository @Inject constructor(
    private val subjectDAO: SubjectDAO
) {

    suspend fun x() {

        subjectDAO.getAllSubjects()
    }
}
package eu.project.aiesla.db.common

import java.util.UUID

data class Subject(
    val nameOfSubject: String,
    val idOfSubject: SubjectID,
    val idsOfContainedNotebooks: List<NotebookID>
)

@JvmInline
value class SubjectID(val id: String) {

    companion object {

        fun generate(): SubjectID =
            SubjectID(id = UUID.randomUUID().toString())
    }
}
package eu.project.aiesla.db.common

import java.util.*

@JvmInline
value class NotebookID(val id: String) {

    companion object {

        fun generate(): NotebookID =
            NotebookID(id = UUID.randomUUID().toString())
    }
}
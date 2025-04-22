package com.example.routesignedin.routeStudy.sharedData.data

import com.example.databases.common.DatabaseManager
import com.example.databases.common.StateOfDataInitialization
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SharedStudyRepositoryBinder @Inject constructor(
    val roomLocalDatabaseManager: DatabaseManager
) {

    suspend fun checkIfLocalDatabaseMustBeInitialized() {

        if (roomLocalDatabaseManager.stateOfDataInitialization.value == StateOfDataInitialization.Idle) {

            val dataMustBeInitialized = roomLocalDatabaseManager.dataMustBeInitialized()

            if (dataMustBeInitialized != null && dataMustBeInitialized) {

                roomLocalDatabaseManager.initializeData()
            }
        }
    }
}
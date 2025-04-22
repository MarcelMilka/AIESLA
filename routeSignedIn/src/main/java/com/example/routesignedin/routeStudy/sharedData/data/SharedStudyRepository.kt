package com.example.routesignedin.routeStudy.sharedData.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SharedStudyRepository @Inject constructor(
    val sharedStudyRepositoryBinder: SharedStudyRepositoryBinder
) {

    suspend fun checkIfLocalDatabaseMustBeInitialized() = sharedStudyRepositoryBinder.checkIfLocalDatabaseMustBeInitialized()
}
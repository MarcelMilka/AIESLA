package com.example.routesignedin.routeStudy.subjects.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedStudyRepository @Inject constructor(
    val sharedStudyRepositoryBinder: SharedStudyRepositoryBinder
)
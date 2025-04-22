package com.example.databases.common

import kotlinx.coroutines.flow.StateFlow

interface DatabaseManager {

    val stateOfDataInitialization: StateFlow<StateOfDataInitialization>

    suspend fun dataMustBeInitialized(): Boolean?

    suspend fun initializeData()
}
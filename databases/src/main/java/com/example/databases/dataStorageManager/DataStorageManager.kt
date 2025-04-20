package com.example.databases.dataStorageManager

import kotlinx.coroutines.flow.StateFlow

interface DataStorageManager {

    val wayOfStoringData: StateFlow<WayOfStoringData>

    suspend fun specifyWayOfStoringData()
}
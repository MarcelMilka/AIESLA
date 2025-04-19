package com.example.databases.dataSynchronizationManager

import kotlinx.coroutines.flow.StateFlow

interface DataSynchronizationManager {

    val dataSynchronizationState: StateFlow<DataSynchronizationState>

    fun changeDataSynchronizationState(state: DataSynchronizationState)
}
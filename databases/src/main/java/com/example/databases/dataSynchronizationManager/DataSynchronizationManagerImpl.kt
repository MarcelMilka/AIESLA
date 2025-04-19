package com.example.databases.dataSynchronizationManager

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataSynchronizationManagerImpl @Inject constructor(): DataSynchronizationManager {

    private var _dataSynchronizationState = MutableStateFlow(DataSynchronizationState.Idle)
    override val dataSynchronizationState = _dataSynchronizationState

    override fun changeDataSynchronizationState(state: DataSynchronizationState) {

        _dataSynchronizationState.value = state
    }
}
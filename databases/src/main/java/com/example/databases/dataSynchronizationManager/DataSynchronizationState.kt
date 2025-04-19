package com.example.databases.dataSynchronizationManager

enum class DataSynchronizationState {
    Idle,
    Synchronized,
    Synchronizing,
    FailedToSynchronize
}
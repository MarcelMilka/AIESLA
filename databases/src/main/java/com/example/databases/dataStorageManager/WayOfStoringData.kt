package com.example.databases.dataStorageManager

import com.example.networkconnectivity.NetworkConnectivityStatus

sealed class WayOfStoringData {

    data object Local: WayOfStoringData()
    data class CloudAndLocal(val networkConnectivityStatus: NetworkConnectivityStatus): WayOfStoringData()
    data object Unidentified: WayOfStoringData()
}
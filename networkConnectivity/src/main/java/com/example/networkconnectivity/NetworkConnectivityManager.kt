package com.example.networkconnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// the class below was implemented following the documentation:
// https://developer.android.com/training/monitoring-device-state/connectivity-status-type

class NetworkConnectivityManager(
    private val context: Context,
    private val ioCoroutine: CoroutineScope
) {

    private val _networkConnectivityStatus = MutableStateFlow(NetworkConnectivityStatus.Disconnected)
    val networkConnectivityStatus = _networkConnectivityStatus.asStateFlow()

//  NetworkRequest - describes app's connection requirements:
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

//  NetworkCallback - receives notifications about changes in the connection status and network capabilities:
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            updateStatus(NetworkConnectivityStatus.Connected)
        }

        override fun onLost(network: Network) {
            updateStatus(NetworkConnectivityStatus.ConnectionLost)
        }

        override fun onUnavailable() {
            updateStatus(NetworkConnectivityStatus.Disconnected)
        }
    }

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//  "Listen" to changes with registerNetworkCallback
    init {

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun updateStatus(status: NetworkConnectivityStatus) {

        ioCoroutine.launch {
            _networkConnectivityStatus.emit(status)
        }
    }
}
package com.example.networkconnectivity

enum class NetworkConnectivityStatus {
    Connected, // when device has access to the network
    Disconnected, // when device does not have access to the network
    ConnectionLost // when device lost access to the network
}
package com.example.databases.common

interface DatabaseManager {

    suspend fun dataMustBeInitialized(): Boolean?

    suspend fun initializeData(): ResultOfDataInitialization
}
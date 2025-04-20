package com.example.databases.dataStorageManager

import com.example.authentication.authentication.Authentication
import com.example.authentication.di.FirebaseAuthenticationQ
import com.example.authentication.di.RoomAuthenticationQ
import com.example.databases.dataSynchronizationManager.DataSynchronizationManager
import com.example.datastore.data.UserOnboardingManager
import com.example.networkconnectivity.NetworkConnectivityManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Manages way of data storage (Local and Cloud or only local)
// Checks if Local and Cloud databases store data for the same user, if not, then information about necessity to synchronize the databases is sent.
// Initialized onCreate MainActivity

internal class DataStorageManagerImpl @Inject constructor(
    val networkConnectivityManager: NetworkConnectivityManager,
    val dataSynchronizationManager: DataSynchronizationManager,

    val userOnboardingManager: UserOnboardingManager,
    @FirebaseAuthenticationQ val firebaseAuthentication: Authentication,
    @RoomAuthenticationQ val roomAuthentication: Authentication,

    val firebaseAuth: FirebaseAuth,
    val coroutineScope: CoroutineScope
): DataStorageManager {

    private var _wayOfStoringData = MutableStateFlow<WayOfStoringData>(WayOfStoringData.Local)
    override val wayOfStoringData = _wayOfStoringData.asStateFlow()

    init {

        // check way of storing data after initialization of the class
        coroutineScope.launch { specifyWayOfStoringData() }
    }

    override suspend fun specifyWayOfStoringData() {

        val userOnboardingState = userOnboardingManager.checkOnboardingState()

        val wayOfStoringData =
            if (userOnboardingState != null) {

                val firstLaunchEver = userOnboardingState.firstLaunchEver
                if (firstLaunchEver != null) {

                    when (firstLaunchEver) {

                        true -> {

                            WayOfStoringData.Local
                        }

                        false -> {

                            when(firebaseAuthentication.isSignedIn()) {

                                true -> {

                                    val networkConnectivityStatus = networkConnectivityManager.networkConnectivityStatus.value
                                    WayOfStoringData.CloudAndLocal(networkConnectivityStatus)
                                }

                                false -> WayOfStoringData.Local
                            }
                        }
                    }
                }

                else WayOfStoringData.Unidentified
            }

            else WayOfStoringData.Unidentified

        _wayOfStoringData.emit(wayOfStoringData)
    }
}


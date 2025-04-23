package eu.project.aiesla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.example.applicationscaffold.applicationScaffold
import com.example.authentication.authenticationManager.AuthenticationManager
import com.example.databases.dataStorageManager.DataStorageManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authenticationManager: AuthenticationManager
    @Inject lateinit var dataStorageManager: DataStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            applicationScaffold(
                authenticationState = authenticationManager.initialAuthenticationState.collectAsState().value
            )
        }
    }
}
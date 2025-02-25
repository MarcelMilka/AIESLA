package eu.project.aiesla.main.routeSignedOut.signUpScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.project.aiesla.auth.Credentials
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun signUpScreen(
    onSignUp: (Credentials) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(Padding.P20.dp),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            TextField(
                value = email,
                onValueChange = {

                    email = it
                }
            )

            TextField(
                value = password,
                onValueChange = {

                    password = it
                }
            )

            Button(
                onClick = {
                    onSignUp(
                        Credentials(
                            email = email,
                            password = password
                        )
                    )
                },
                content = {}
            )
        }
    )
}
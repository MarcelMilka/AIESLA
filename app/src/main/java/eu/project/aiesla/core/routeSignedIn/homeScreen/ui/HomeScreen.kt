package eu.project.aiesla.core.routeSignedIn.homeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun homeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(Padding.P20.dp),

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            // upper part
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    Text("HomeScreen")
                }
            )

            // lower part
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {}
            )
        }
    )
}
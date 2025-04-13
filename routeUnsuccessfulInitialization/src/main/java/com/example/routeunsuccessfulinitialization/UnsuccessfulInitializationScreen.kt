package com.example.routeunsuccessfulinitialization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun unsuccessfulInitializationScreen() {

    Column(
        modifier = Modifier.fillMaxSize().testTag("UnsuccessfulInitializationScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Text("UnsuccessfulInitializationScreen")
        }
    )
}
package eu.project.aiesla.main.routeSignedOut.checkYourEmail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.text.bigPrimaryLabel
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun checkYourEmailScreen() {

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

                    bigPrimaryLabel(
                        content = stringResource(R.string.check_your_email)
                    )
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
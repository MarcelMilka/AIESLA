package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUpEmailInformation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.primaryAuthenticationButton
import eu.project.aiesla.sharedUi.sharedElements.text.bigPrimaryLabel
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun signUpEmailInformationScreen(onSignIn: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20.dp)
            .testTag("SignUpEmailInformationScreen"),

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
                        content = stringResource(R.string.verify_your_email)
                    )
                }
            )

            // lower part
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    // sign in
                    primaryAuthenticationButton(
                        content = stringResource(R.string.sign_in),
                        testTag = "SignUpEmailInformationScreen primaryAuthenticationButton 'Sign in.'",
                        onClick = {

                            onSignIn()
                        }
                    )
                }
            )
        }
    )
}
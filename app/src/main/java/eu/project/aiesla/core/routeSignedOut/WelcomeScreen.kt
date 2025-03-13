package eu.project.aiesla.core.routeSignedOut

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.primaryAuthenticationButton
import eu.project.aiesla.sharedUi.sharedElements.verticalDividerS
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun welcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
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
                content = {}
            )

            // lower part
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    // sign in / sign up
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {

                            // sign in
                            primaryAuthenticationButton(
                                content = stringResource(R.string.sign_in),
                                testTag = "WelcomeScreen primaryAuthenticationTextButton 'Sign in.'",
                                onClick = {onSignIn()}
                            )

                            verticalDividerS()

                            // sign up
                            primaryAuthenticationButton(
                                content = stringResource(R.string.sign_up),
                                testTag = "WelcomeScreen primaryAuthenticationTextButton 'Sign up.'",
                                onClick = {onSignUp()}
                            )
                        }
                    )
                }
            )
        }
    )
}
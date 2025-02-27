package eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.primaryAuthenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.button.secondaryAuthenticationTextButton
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun welcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
) {

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
                content = {}
            )

            // lower part
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
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
                            primaryAuthenticationTextButton(
                                content = stringResource(R.string.sign_in),
                                testTag = "button 'Sign in.'",
                                onClick = {onSignIn()}
                            )

                            HorizontalDivider(modifier = Modifier.fillMaxWidth())

                            // sign up
                            primaryAuthenticationTextButton(
                                content = stringResource(R.string.sign_up),
                                testTag = "button 'Sign up.'",
                                onClick = {onSignUp()}
                            )
                        }
                    )

                    // continue without account
                    secondaryAuthenticationTextButton(
                        content = stringResource(R.string.continue_without_account),
                        testTag = "button 'Continue without account.'",
                        onClick = {onContinueWithoutAccount()}
                    )
                }
            )
        }
    )
}
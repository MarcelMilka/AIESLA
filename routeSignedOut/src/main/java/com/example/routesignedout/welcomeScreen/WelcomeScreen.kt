package com.example.routesignedout.welcomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.sharedui.R
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.button.primaryAuthenticationButton
import com.example.sharedui.sharedUiElements.divider.verticalDivider5
import com.example.sharedui.theme.Background

@Composable
internal fun welcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20)
            .testTag("WelcomeScreen"),

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

                    // Buttons "Sign in." & "Sign up."
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {

                            // "Sign in."
                            primaryAuthenticationButton(
                                content = stringResource(R.string.sign_in),
                                testTag = "WelcomeScreen - button 'Sign in.'",
                                onClick = { onSignIn() }
                            )

                            verticalDivider5()

                            // "Sign up."
                            primaryAuthenticationButton(
                                content = stringResource(R.string.sign_up),
                                testTag = "WelcomeScreen - button 'Sign up.'",
                                onClick = { onSignUp() }
                            )
                        }
                    )
                }
            )
        }
    )
}
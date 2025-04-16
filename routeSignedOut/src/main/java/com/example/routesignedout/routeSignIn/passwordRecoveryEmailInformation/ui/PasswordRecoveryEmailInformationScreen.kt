package com.example.routesignedout.routeSignIn.passwordRecoveryEmailInformation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.sharedui.sharedUiElements.text.guidanceText
import com.example.sharedui.theme.Background
import com.example.sharedui.R
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.button.primaryAuthenticationButton

@Composable
internal fun passwordRecoveryEmailInformationScreen(onSignIn: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20)
            .testTag("PasswordRecoveryEmailInformationScreen"),

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

                    guidanceText(
                        content = stringResource(R.string.check_your_email)
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

                    primaryAuthenticationButton(
                        content = stringResource(R.string.sign_in),
                        testTag = "PasswordRecoveryEmailInformationScreen primaryAuthenticationTextButton 'Sign in.'",
                        onClick = { onSignIn() }
                    )
                }
            )
        }
    )
}
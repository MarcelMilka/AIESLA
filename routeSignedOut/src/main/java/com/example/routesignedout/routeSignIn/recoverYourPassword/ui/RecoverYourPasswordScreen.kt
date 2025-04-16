package com.example.routesignedout.routeSignIn.recoverYourPassword.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.authentication.credentials.EmailCredential
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintViewState
import com.example.routesignedout.routeSignIn.signIn.ui.elements.signInEmailHint
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import com.example.sharedui.sharedUiElements.button.buttonProceed
import com.example.sharedui.sharedUiElements.divider.verticalDivider20
import com.example.sharedui.sharedUiElements.textField.emailTextField
import com.example.sharedui.theme.Background
import com.example.sharedui.R

@Composable
internal fun recoverYourPasswordScreen(
    email: EmailCredential,
    onUpdateEmail: (EmailCredential) -> Unit,

    emailHintViewState: SignInEmailHintViewState,
    buttonProceedViewState: ButtonProceedViewState,

    onRecoverPassword: () -> Unit,
) {

    val emailFocusRequester = remember { FocusRequester() }

    // activate email text field immediately after entering the screen
    LaunchedEffect(true) { emailFocusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20)
            .testTag("RecoverYourPasswordScreen"),

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

                    emailTextField(
                        email = email.email,
                        testTag = "RecoverYourPasswordScreen emailTextField",
                        onValueChange = { onUpdateEmail(EmailCredential(email = it)) },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    signInEmailHint(viewState = emailHintViewState)

                    verticalDivider20()

                    buttonProceed(
                        content = stringResource(R.string.recover_your_password),
                        testTag = "RecoverYourPasswordScreen `recover your password`",
                        buttonProceedViewState = buttonProceedViewState,
                        onClick = { onRecoverPassword() }
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
                content = {}
            )
        }
    )
}
package com.example.routesignedout.routeSignIn.signIn.ui

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
import com.example.authentication.credentials.EmailAndPasswordCredentials
import com.example.authentication.credentials.EmailCredential
import com.example.authentication.credentials.PasswordCredential
import com.example.routesignedout.routeSignIn.signIn.model.SignInEmailHintViewState
import com.example.routesignedout.routeSignIn.signIn.model.SignInPasswordHintViewState
import com.example.routesignedout.routeSignIn.signIn.ui.elements.signInEmailHint
import com.example.routesignedout.routeSignIn.signIn.ui.elements.signInPasswordHint
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import com.example.sharedui.sharedUiElements.button.buttonProceed
import com.example.sharedui.sharedUiElements.divider.verticalDivider10
import com.example.sharedui.sharedUiElements.divider.verticalDivider20
import com.example.sharedui.textField.passwordTextField
import com.example.sharedui.theme.Background
import com.example.sharedui.R
import com.example.sharedui.sharedUiElements.button.authenticationTextButton
import com.example.sharedui.sharedUiElements.textField.emailTextField

@Composable
internal fun signInScreen(
    credentials: EmailAndPasswordCredentials,

    onUpdateEmail: (EmailCredential) -> Unit,
    onUpdatePassword: (PasswordCredential) -> Unit,

    emailHintViewState: SignInEmailHintViewState,
    passwordHintViewState: SignInPasswordHintViewState,
    buttonProceedViewState: ButtonProceedViewState,

    onSignIn: () -> Unit,
    onRecoverPassword: () -> Unit,
) {

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // activate email text field immediately after entering the screen
    LaunchedEffect(true) { emailFocusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20)
            .testTag("SignInScreen"),

        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            // upper part
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,

                content = {

                    emailTextField(
                        email = credentials.email,
                        testTag = "SignInScreen emailTextField",
                        onValueChange = { onUpdateEmail(EmailCredential(email = it)) },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    signInEmailHint(viewState = emailHintViewState)

                    verticalDivider10()

                    passwordTextField(
                        password = credentials.password,
                        testTag = "SignInScreen passwordTextField",
                        onValueChange = { onUpdatePassword(PasswordCredential(password = it)) },
                        assignedFocusRequester = passwordFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    signInPasswordHint(viewState = passwordHintViewState)

                    verticalDivider20()

                    buttonProceed(
                        content = stringResource(R.string.sign_in),
                        testTag = "SignInScreen button 'Sign in'",
                        buttonProceedViewState = buttonProceedViewState,
                        onClick = { onSignIn() }
                    )

                    authenticationTextButton(
                        content = stringResource(R.string.recover_your_password),
                        testTag = "SignInScreen button 'Recover your password",
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
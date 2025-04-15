package com.example.routesignedout.routeSignUp.signUp.ui

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
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintViewState
import com.example.routesignedout.routeSignUp.signUp.model.SignUpPasswordHintViewState
import com.example.routesignedout.routeSignUp.signUp.ui.elements.signUpEmailTextFieldHint
import com.example.routesignedout.routeSignUp.signUp.ui.elements.signUpPasswordHint
import com.example.sharedui.dimensions.Padding
import com.example.sharedui.sharedUiElements.button.ButtonProceedViewState
import com.example.sharedui.sharedUiElements.button.buttonProceed
import com.example.sharedui.sharedUiElements.divider.verticalDivider10
import com.example.sharedui.sharedUiElements.divider.verticalDivider20
import com.example.sharedui.sharedUiElements.textField.emailTextField
import com.example.sharedui.theme.Background
import com.example.sharedui.R.string
import com.example.sharedui.textField.passwordTextField

@Composable
internal fun signUpScreen(
    credentials: EmailAndPasswordCredentials,

    onUpdateEmail: (EmailCredential) -> Unit,
    onUpdatePassword: (PasswordCredential) -> Unit,
    onFocusChanged: (Boolean) -> Unit,

    emailHintViewState: SignUpEmailHintViewState,
    passwordHintViewState: SignUpPasswordHintViewState,
    buttonProceedViewState: ButtonProceedViewState,

    onSignUp: () -> Unit
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
            .testTag("SignUpScreen"),

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
                        email = credentials.email,
                        testTag = "SignUpScreen - emailTextField",
                        onValueChange = { onUpdateEmail(EmailCredential(email = it)) },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    signUpEmailTextFieldHint(viewState = emailHintViewState)

                    verticalDivider10()

                    passwordTextField(
                        password = credentials.password,
                        testTag = "SignUpScreen - passwordTextField",
                        onValueChange = { onUpdatePassword(PasswordCredential(password = it)) },
                        assignedFocusRequester = passwordFocusRequester,
                        onFocusChanged = { onFocusChanged(it) },
                        onDone = {}
                    )

                    signUpPasswordHint(viewState = passwordHintViewState)

                    verticalDivider20()

                    buttonProceed(
                        content = stringResource(string.sign_up),
                        buttonProceedViewState = buttonProceedViewState,
                        testTag = "SignUpScreen button 'Sign up'",
                        onClick = { onSignUp() }
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
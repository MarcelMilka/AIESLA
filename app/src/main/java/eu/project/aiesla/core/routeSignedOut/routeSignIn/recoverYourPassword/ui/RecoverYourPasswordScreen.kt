package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.ButtonProceedViewState
import eu.project.aiesla.sharedUi.sharedElements.button.buttonProceed
import eu.project.aiesla.sharedUi.sharedElements.textField.SignInEmailHintViewState
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.textField.signInEmailHint
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider20
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun recoverYourPasswordScreen(
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
            .padding(Padding.P20.dp)
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
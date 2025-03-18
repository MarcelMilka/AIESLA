package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn.ui

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
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.credentials.PasswordCredential
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.ButtonProceedViewState
import eu.project.aiesla.sharedUi.sharedElements.button.authenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.button.buttonProceed
import eu.project.aiesla.sharedUi.sharedElements.textField.*
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider10
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider20
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun signInScreen(
    credentials: EmailAndPasswordCredentials,

    onUpdateEmail: (EmailCredential) -> Unit,
    onUpdatePassword: (PasswordCredential) -> Unit,

    emailHintViewState: EmailTextFieldViewState,
    passwordHintViewState: PasswordTextFieldViewState,
    buttonProceedViewState: ButtonProceedViewState,

    onSignIn: () -> Unit,
    onRecoverPassword: () -> Unit,
) {

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // activate email text field immediately after entering the screen
    LaunchedEffect(true) {

        emailFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20.dp)
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
                        onValueChange = {

                            onUpdateEmail(EmailCredential(email = it))
                        },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    emailTextFieldHintImpl(viewState = emailHintViewState)

                    verticalDivider10()

                    passwordTextField(
                        password = credentials.password,
                        testTag = "SignInScreen passwordTextField",
                        onValueChange = {

                            onUpdatePassword(PasswordCredential(password = it))
                        },
                        assignedFocusRequester = passwordFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    // password hint
                    passwordTextFieldHintImpl(viewState = passwordHintViewState)

                    verticalDivider20()

                    // 'sign in'
                    buttonProceed(
                        content = stringResource(R.string.sign_in),
                        testTag = "SignInScreen dynamicAuthenticationButton",
                        buttonProceedViewState = buttonProceedViewState,
                        onClick = {

                            onSignIn()
                        }
                    )

                    // 'recover your password'
                    authenticationTextButton(
                        content = stringResource(R.string.recover_your_password),
                        testTag = "SignInScreen authenticationTextButton",
                        onClick = {onRecoverPassword()}
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
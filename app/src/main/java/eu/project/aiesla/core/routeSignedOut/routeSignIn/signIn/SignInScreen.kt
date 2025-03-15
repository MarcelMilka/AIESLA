package eu.project.aiesla.core.routeSignedOut.routeSignIn.signIn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.buttonSignInSignUp
import eu.project.aiesla.sharedUi.sharedElements.button.authenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.textField.passwordTextField
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider10
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider20
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider5
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun signInScreen(
    onSignIn: (EmailAndPasswordCredentials) -> Unit,
    onRecoverPassword: () -> Unit,
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    emailTextField(
                        email = email,
                        testTag = "SignInScreen emailTextField",
                        onValueChange = { email = it },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    AnimatedVisibility(
                        visible = false,
                        content = {

                            Column(
                                modifier = Modifier.width(300.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {

                                    verticalDivider5()

                                    textFieldHint(
                                        content = "stringResource(R.string.email_is_already_in_use)",
                                        testTag = "SignInScreen emailTextFieldHint"
                                    )
                                }
                            )
                        }
                    )

                    verticalDivider10()

                    passwordTextField(
                        password = password,
                        testTag = "SignInScreen passwordTextField",
                        onValueChange = { password = it },
                        assignedFocusRequester = passwordFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    AnimatedVisibility(
                        visible = false,
                        content = {

                            Column(
                                modifier = Modifier.width(300.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {

                                    verticalDivider5()

                                    textFieldHint(
                                        content = "stringResource(R.string.email_is_already_in_use)",
                                        testTag = "SignInScreen passwordTextFieldHint"
                                    )
                                }
                            )
                        }
                    )

                    verticalDivider20()

                    buttonSignInSignUp(
                        content = stringResource(R.string.sign_in),
                        testTag = "SignInScreen dynamicAuthenticationButton",
                        enabled = email.isNotEmpty() && password.isNotEmpty(),
                        onClick = {

                            onSignIn(
                                EmailAndPasswordCredentials(
                                    email = email,
                                    password = password
                                )
                            )
                        }
                    )

                    // recover your password
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
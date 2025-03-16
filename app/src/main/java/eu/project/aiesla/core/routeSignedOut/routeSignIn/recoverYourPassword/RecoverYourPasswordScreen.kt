package eu.project.aiesla.core.routeSignedOut.routeSignIn.recoverYourPassword

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
import eu.project.aiesla.auth.credentials.EmailCredential
import eu.project.aiesla.auth.results.PasswordRecoveryProcess
import eu.project.aiesla.auth.results.UnsuccessfulPasswordRecoveryCause
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.buttonSignInSignUp
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider20
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider5
import eu.project.aiesla.sharedUi.theme.Background

@Composable
fun recoverYourPasswordScreen(
    passwordRecoveryProcess: PasswordRecoveryProcess,
    onResetPasswordRecoveryProcess: () -> Unit,
    onRecoverPassword: (EmailCredential) -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }

    // activate email text field immediately after entering the screen
    LaunchedEffect(true) {

        emailFocusRequester.requestFocus()
    }

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
                        email = email,
                        testTag = "RecoverYourPasswordScreen emailTextField",
                        onValueChange = {

                            email = it
                            onResetPasswordRecoveryProcess()
                        },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    AnimatedVisibility(
                        visible = passwordRecoveryProcess is PasswordRecoveryProcess.Unsuccessful,
                        content = {

                            Column(
                                modifier = Modifier.width(300.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {

                                    verticalDivider5()

                                    when (passwordRecoveryProcess) {

                                        PasswordRecoveryProcess.Idle -> {}

                                        PasswordRecoveryProcess.Pending -> {}

                                        PasswordRecoveryProcess.Successful -> {}

                                        is PasswordRecoveryProcess.Unsuccessful -> {

                                            when (passwordRecoveryProcess.cause) {

                                                UnsuccessfulPasswordRecoveryCause.InvalidEmailFormat -> {

                                                    textFieldHint(
                                                        content = stringResource(R.string.invalid_email_address),
                                                        testTag = "RecoverYourPasswordScreen emailTextFieldHint InvalidEmailFormat"
                                                    )
                                                }

                                                UnsuccessfulPasswordRecoveryCause.Timeout -> {}

                                                UnsuccessfulPasswordRecoveryCause.UnidentifiedException -> {

                                                    textFieldHint(
                                                        content = stringResource(R.string.unidentified_error),
                                                        testTag = "RecoverYourPasswordScreen emailTextFieldHint UnidentifiedException"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    )

                    verticalDivider20()

                    buttonSignInSignUp(
                        content = stringResource(R.string.recover_your_password),
                        testTag = "RecoverYourPasswordScreen dynamicAuthenticationButton",
                        enabled = email.isNotEmpty() && passwordRecoveryProcess !is PasswordRecoveryProcess.Unsuccessful,
                        onClick = {

                            onRecoverPassword(
                                EmailCredential(email = email)
                            )
                        }
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
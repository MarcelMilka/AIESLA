package eu.project.aiesla.core.routeSignedOut.signIn.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.primaryAuthenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.button.secondaryAuthenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.textField.passwordTextField
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun signInScreen(
    onSignIn: (EmailAndPasswordCredentials) -> Unit,
    onRecoverPassword: () -> Unit,
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val keyboardIsVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(Padding.P20.dp)
            .imePadding(),

        verticalArrangement = if (keyboardIsVisible) Arrangement.Center else Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (keyboardIsVisible) 0.7f else 0.5f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,

                content = {

                    // email text field, password text field, password requirements, button sign up
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Max),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        content = {

                            emailTextField(
                                value = email,
                                onValueChange = {email = it},
                                focusRequester = emailFocusRequester,
                                nextFocusRequester = passwordFocusRequester,
                            )

                            HorizontalDivider(modifier = Modifier.fillMaxWidth())

                            passwordTextField(
                                value = password,
                                onValueChange = {password = it},
                                focusRequester = passwordFocusRequester,
                            )

                            Spacer(Modifier.height(10.dp))

                            AnimatedVisibility(
                                visible = password.length >= 8 && email.isNotEmpty(),
                                enter = fadeIn(animationSpec = tween(300)),
                                exit = fadeOut(animationSpec = tween(300)),
                                content = {

                                    primaryAuthenticationTextButton(
                                        content = stringResource(R.string.sign_in),
                                        testTag = "button 'Sign in.'",
                                        onClick = {

                                            onSignIn(
                                                EmailAndPasswordCredentials(
                                                    email = email,
                                                    password = password
                                                )
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )

                    // recover your password
                    secondaryAuthenticationTextButton(
                        content = stringResource(R.string.recover_your_password),
                        testTag = "button 'Recover your password.'",
                        onClick = {onRecoverPassword()}
                    )
                }
            )
        }
    )
}
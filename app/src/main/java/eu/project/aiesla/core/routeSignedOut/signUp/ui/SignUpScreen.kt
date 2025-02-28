package eu.project.aiesla.core.routeSignedOut.signUp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import eu.project.aiesla.auth.EmailAndPasswordCredentials
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.theme.DarkBackground
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import eu.project.aiesla.R
import eu.project.aiesla.auth.PasswordRequirements
import eu.project.aiesla.sharedUi.sharedElements.button.primaryAuthenticationTextButton
import eu.project.aiesla.sharedUi.sharedElements.text.primaryCenteredLabel50
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.textField.passwordTextField

@Composable
fun signUpScreen(
    onSignUp: (EmailAndPasswordCredentials) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    var allCharactersCount by rememberSaveable { mutableStateOf(0) }
    var uppercaseCount by rememberSaveable { mutableStateOf(0) }
    var specialCharCount by rememberSaveable { mutableStateOf(0) }
    var digitCount by rememberSaveable { mutableStateOf(0) }

    fun updatePasswordRequirements(password: String) {
        allCharactersCount = password.length
        uppercaseCount = password.count { it.isUpperCase() }
        specialCharCount = password.count { !it.isLetterOrDigit() }
        digitCount = password.count { it.isDigit() }
    }

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
                                onValueChange = { email = it },
                                focusRequester = emailFocusRequester,
                                nextFocusRequester = passwordFocusRequester,
                            )

                            HorizontalDivider(modifier = Modifier.fillMaxWidth())

                            passwordTextField(
                                value = password,
                                onValueChange = {

                                    password = it
                                    updatePasswordRequirements(it)
                                },
                                focusRequester = passwordFocusRequester,
                            )

                            Spacer(Modifier.height(10.dp))

                            primaryCenteredLabel50(content = "$allCharactersCount/${PasswordRequirements.MIN_CHARACTERS_COUNT} characters")
                            primaryCenteredLabel50(content = "$uppercaseCount/${PasswordRequirements.MIN_UPPERCASE_COUNT} uppercase letter")
                            primaryCenteredLabel50(content = "$specialCharCount/${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} special character")
                            primaryCenteredLabel50(content = "$digitCount/${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} numeric character")

                            Spacer(Modifier.height(10.dp))

                            AnimatedVisibility(
                                visible = allCharactersCount >= 8 && uppercaseCount >= 1 && specialCharCount >= 1 && digitCount >= 1 && email.isNotEmpty(),
                                enter = fadeIn(animationSpec = tween(300)),
                                exit = fadeOut(animationSpec = tween(300)),
                                content = {

                                    primaryAuthenticationTextButton(
                                        content = stringResource(R.string.sign_up),
                                        testTag = "button 'Sign up.'",
                                        onClick = {

                                            onSignUp(
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
                }
            )
        }
    )
}
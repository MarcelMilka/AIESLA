package eu.project.aiesla.core.routeSignedOut.routeSignUp.signUp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.auth.credentials.EmailAndPasswordCredentials
import eu.project.aiesla.auth.credentials.PasswordRequirements
import eu.project.aiesla.auth.results.SignUpProcess
import eu.project.aiesla.auth.results.UnsuccessfulSignUpProcessCause
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.button.buttonSignInSignUp
import eu.project.aiesla.sharedUi.sharedElements.text.dynamicTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldHint
import eu.project.aiesla.sharedUi.sharedElements.textField.emailTextField
import eu.project.aiesla.sharedUi.sharedElements.textField.passwordTextField
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider10
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider20
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider5
import eu.project.aiesla.sharedUi.theme.Background
import eu.project.aiesla.sharedUi.theme.SecondaryWhite

@Composable
fun signUpScreen(
    signUpProcess: SignUpProcess,
    onSignUp: (EmailAndPasswordCredentials) -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    var passwordTextFieldIsActive by remember { mutableStateOf(false) }

    // activate email text field immediately after entering the screen
    LaunchedEffect(true) {

        emailFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Padding.P20.dp)
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
                        email = email,
                        testTag = "SignUpScreen emailTextField",
                        onValueChange = { email = it },
                        emailFocusRequester = emailFocusRequester,
                        onFocusChanged = {},
                        onDone = {}
                    )

                    AnimatedVisibility(
                        visible = signUpProcess is SignUpProcess.Unsuccessful,
                        content = {

                            Column(
                                modifier = Modifier.width(300.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {

                                    verticalDivider5()

                                    when (signUpProcess is SignUpProcess.Unsuccessful) {

                                        true -> {

                                            when(signUpProcess.cause) {

                                                UnsuccessfulSignUpProcessCause.InvalidEmailFormat -> {

                                                    textFieldHint(
                                                        content = stringResource(R.string.invalid_email_address),
                                                        testTag = "SignUpScreen emailTextFieldHint InvalidEmailFormat"
                                                    )
                                                }

                                                UnsuccessfulSignUpProcessCause.EmailIsAlreadyInUse -> {

                                                    textFieldHint(
                                                        content = stringResource(R.string.email_is_already_in_use),
                                                        testTag = "SignUpScreen emailTextFieldHint EmailIsAlreadyInUse"
                                                    )
                                                }

                                                UnsuccessfulSignUpProcessCause.Timeout -> {}

                                                UnsuccessfulSignUpProcessCause.UnidentifiedException -> {

                                                    textFieldHint(
                                                        content = stringResource(R.string.unidentified_error),
                                                        testTag = "SignUpScreen emailTextFieldHint UnidentifiedException"
                                                    )
                                                }
                                            }
                                        }

                                        false -> {}
                                    }
                                }
                            )
                        }
                    )

                    verticalDivider10()

                    passwordTextField(
                        password = password,
                        testTag = "SignUpScreen passwordTextField",
                        onValueChange = { password = it },
                        assignedFocusRequester = passwordFocusRequester,
                        onFocusChanged = {passwordTextFieldIsActive = it},
                        onDone = {}
                    )

                    AnimatedVisibility(
                        visible = passwordTextFieldIsActive,
                        content = {

                            Column (
                                modifier = Modifier.width(300.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {

                                    verticalDivider5()

                                    passwordRequirement(
                                        isGreen = password.count() >= PasswordRequirements.MIN_CHARACTERS_COUNT,
                                        content = "${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${stringResource(R.string.characters)}",
                                        testTag = "SignUpScreen passwordRequirement 'At least x characters'"
                                    )

                                    passwordRequirement(
                                        isGreen = password.count { it.isUpperCase() } >= PasswordRequirements.MIN_UPPERCASE_COUNT,
                                        content = "${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${stringResource(R.string.uppercase_letters)}",
                                        testTag = "SignUpScreen passwordRequirement 'At least x uppercase characters'"
                                    )

                                    passwordRequirement(
                                        isGreen = password.count { !it.isLetterOrDigit() } >= PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT,
                                        content = "${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${stringResource(R.string.special_character)}",
                                        testTag = "SignUpScreen passwordRequirement 'At least x special character'"
                                    )

                                    passwordRequirement(
                                        isGreen = password.count { it.isDigit() } >= PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT,
                                        content = "${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${stringResource(R.string.numeric_character)}",
                                        testTag = "SignUpScreen passwordRequirement 'At least x numeric character'"
                                    )
                                }
                            )
                        }
                    )

                    verticalDivider20()

                    buttonSignInSignUp(
                        content = stringResource(R.string.sign_up),
                        testTag = "SignUpScreen dynamicAuthenticationButton",
                        enabled =
                            email.isNotEmpty() &&
                            password.count() >= PasswordRequirements.MIN_CHARACTERS_COUNT &&
                            password.count { it.isUpperCase() } >= PasswordRequirements.MIN_UPPERCASE_COUNT &&
                            password.count { !it.isLetterOrDigit() } >= PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT &&
                            password.count { it.isDigit() } >= PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT,
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

@Composable
private fun passwordRequirement(isGreen: Boolean, content: String, testTag: String) {

    var targetColor by remember { mutableStateOf(SecondaryWhite) }

    targetColor = when (isGreen) {
        true -> Green
        false -> SecondaryWhite
    }

    val color by animateColorAsState(targetValue = targetColor)

    dynamicTextFieldHint(
        content = content,
        color = color,
        testTag = testTag
    )
}
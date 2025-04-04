package eu.project.aiesla.sharedUi.sharedElements.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.auth.credentials.PasswordRequirements
import eu.project.aiesla.sharedConstants.RoundedCorner
import eu.project.aiesla.sharedUi.sharedElements.text.dynamicTextFieldHint
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldHint
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldLabel
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider5
import eu.project.aiesla.sharedUi.theme.EmailPasswordTextFieldTextStyle
import eu.project.aiesla.sharedUi.theme.Primary
import eu.project.aiesla.sharedUi.theme.SecondaryWhite

@Composable
fun passwordTextField (
    password: String,
    testTag: String,
    onValueChange: (String) -> Unit,
    assignedFocusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit
) {

    var passwordIsVisible by rememberSaveable {

        mutableStateOf(false)
    }

    TextField(

        value = password,
        onValueChange = {

            onValueChange(it)
        },

        label = {

            textFieldLabel(
                content = stringResource(R.string.password)
            )
        },

        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .testTag(testTag)
            .focusRequester(assignedFocusRequester)
            .onFocusChanged {

                if (it.isFocused) {

                    onFocusChanged(true)
                }

                else if (!it.isFocused){

                    onFocusChanged(false)
                }
            },

        colors = TextFieldDefaults.colors(

            unfocusedLabelColor = White,
            focusedLabelColor = White,

            focusedContainerColor = Primary,
            unfocusedContainerColor = Primary,

            cursorColor = White,

            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent,

            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledContainerColor = Transparent,
            focusedIndicatorColor = Transparent
        ),

        textStyle = EmailPasswordTextFieldTextStyle,

        shape = RoundedCornerShape(RoundedCorner.MEDIUM.dp),

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            capitalization = KeyboardCapitalization.None
        ),

        keyboardActions = KeyboardActions(

            onDone = {

                onDone()
            }
        ),

        trailingIcon = {

            IconButton(

                modifier = Modifier.testTag("PasswordTextField icon button"),

                onClick = {

                    passwordIsVisible = !passwordIsVisible
                },

                content = {

                    when (passwordIsVisible) {

                        true -> {

                            Icon(
                                painter = painterResource(R.drawable.visibility_on),
                                contentDescription = "Icon 'visibility on'",
                                tint = White
                            )
                        }

                        false -> {

                            Icon(
                                painter = painterResource(R.drawable.visibility_off),
                                contentDescription = "Icon 'visibility off'",
                                tint = White
                            )
                        }
                    }
                },
            )
        },

        singleLine = true,

        visualTransformation =
            if (passwordIsVisible) { VisualTransformation.None }
            else { PasswordVisualTransformation() },
    )
}

@Composable
fun signInPasswordHint(viewState: SignInPasswordHintViewState) {

    AnimatedVisibility(
        visible = viewState is SignInPasswordHintViewState.Visible,
        content = {

            Column(
                modifier = Modifier.width(300.dp).testTag("PasswordTextFieldHint impl"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    verticalDivider5()

                    when(viewState) {

                        is SignInPasswordHintViewState.Visible -> {

                            val content =
                                when(viewState.hint){
                                    SignInPasswordHintOptions.PasswordIsIncorrect -> stringResource(R.string.invalid_password)
                                    SignInPasswordHintOptions.Timeout -> stringResource(R.string.timeout)
                                    SignInPasswordHintOptions.UnidentifiedException -> stringResource(R.string.unidentified_error)
                                }

                            textFieldHint(
                                content = content,
                                testTag = "PasswordTextFieldHint"
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    )
}

sealed class SignInPasswordHintViewState {

    data object Invisible: SignInPasswordHintViewState()
    data class Visible(val hint: SignInPasswordHintOptions): SignInPasswordHintViewState()
}

enum class SignInPasswordHintOptions {
    PasswordIsIncorrect,
    Timeout,
    UnidentifiedException,
}



@Composable
fun signUpPasswordHint(viewState: SignUpPasswordHintViewState) {

    AnimatedVisibility(
        visible = viewState is SignUpPasswordHintViewState.Visible,
        content = {

            Column(
                modifier = Modifier.width(300.dp).testTag("SignUpPasswordTextFieldHint impl"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    verticalDivider5()

                    when(viewState) {

                        is SignUpPasswordHintViewState.Visible -> {

                            passwordRequirement(
                                isGreen = viewState.totalCharacters.isGreen,
                                content = "${viewState.totalCharacters.currentCount}/${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${stringResource(R.string.characters)}",
                                testTag = "totalCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.uppercaseCharacters.isGreen,
                                content = "${viewState.uppercaseCharacters.currentCount}/${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${stringResource(R.string.uppercase_letters)}",
                                testTag = "uppercaseCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.specialCharacters.isGreen,
                                content = "${viewState.specialCharacters.currentCount}/${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${stringResource(R.string.special_character)}",
                                testTag = "specialCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.numericCharacters.isGreen,
                                content = "${viewState.numericCharacters.currentCount}/${stringResource(R.string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${stringResource(R.string.numeric_character)}",
                                testTag = "numericCharacters"
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    )
}

sealed class SignUpPasswordHintViewState {

    data object Invisible: SignUpPasswordHintViewState()
    data class Visible(
        val totalCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val uppercaseCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val specialCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0),
        val numericCharacters: PasswordRequirementViewState = PasswordRequirementViewState(false, 0)
    ): SignUpPasswordHintViewState()
}



data class PasswordRequirementViewState(val isGreen: Boolean = false, val currentCount: Int = 0)

@Composable
fun passwordRequirement(isGreen: Boolean, content: String, testTag: String) {

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

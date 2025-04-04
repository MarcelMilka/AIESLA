package eu.project.aiesla.sharedUi.sharedElements.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.RoundedCorner
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldHint
import eu.project.aiesla.sharedUi.sharedElements.text.textFieldLabel
import eu.project.aiesla.sharedUi.sharedElements.verticalDivider5
import eu.project.aiesla.sharedUi.theme.EmailPasswordTextFieldTextStyle
import eu.project.aiesla.sharedUi.theme.Primary

@Composable
fun emailTextField (
    email: String,
    testTag: String,
    onValueChange: (String) -> Unit,
    emailFocusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit
) {

    TextField(

        value = email,
        onValueChange = {

            onValueChange(it)
        },

        label = {

            textFieldLabel(
                content = stringResource(R.string.email)
            )
        },

        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .testTag(testTag)
            .focusRequester(emailFocusRequester)
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
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None
        ),

        keyboardActions = KeyboardActions(

            onDone = {

                onDone()
            },
        )
    )
}

@Composable
fun signInEmailHint(viewState: SignInEmailHintViewState) {

    AnimatedVisibility(
        visible = viewState is SignInEmailHintViewState.Visible,
        content = {

            Column(
                modifier = Modifier.width(300.dp).testTag("EmailTextFieldHint impl"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    verticalDivider5()

                    when(viewState) {

                        is SignInEmailHintViewState.Visible -> {

                            val content =
                                when(viewState.hint){
                                    SignInEmailHintOptions.InvalidEmailFormat -> stringResource(R.string.invalid_email_address)
                                    SignInEmailHintOptions.Timeout -> stringResource(R.string.timeout)
                                    SignInEmailHintOptions.UnidentifiedException -> stringResource(R.string.unidentified_error)
                                }

                            textFieldHint(
                                content = content,
                                testTag = "EmailTextFieldHint"
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    )
}

sealed class SignInEmailHintViewState {

    data object Invisible: SignInEmailHintViewState()
    data class Visible(val hint: SignInEmailHintOptions): SignInEmailHintViewState()
}

enum class SignInEmailHintOptions {
    InvalidEmailFormat,
    Timeout,
    UnidentifiedException,
}



@Composable
fun signUpEmailTextFieldHintImpl(viewState: SignUpEmailHintViewState) {

    AnimatedVisibility(
        visible = viewState is SignUpEmailHintViewState.Visible,
        content = {

            Column(
                modifier = Modifier.width(300.dp).testTag("SignUpEmailTextFieldHint impl"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    verticalDivider5()

                    when(viewState) {

                        is SignUpEmailHintViewState.Visible -> {

                            val content =
                                when(viewState.hint){
                                    SignUpEmailHintOptions.InvalidEmailFormat -> stringResource(R.string.invalid_email_address)
                                    SignUpEmailHintOptions.EmailIsAlreadyInUse -> stringResource(R.string.email_is_already_in_use)
                                    SignUpEmailHintOptions.Timeout -> stringResource(R.string.timeout)
                                    SignUpEmailHintOptions.UnidentifiedException -> stringResource(R.string.unidentified_error)
                                }

                            textFieldHint(
                                content = content,
                                testTag = "SignUpEmailTextFieldHint"
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    )
}

sealed class SignUpEmailHintViewState {

    data object Invisible: SignUpEmailHintViewState()
    data class Visible(val hint: SignUpEmailHintOptions): SignUpEmailHintViewState()
}

enum class SignUpEmailHintOptions {
    InvalidEmailFormat,
    EmailIsAlreadyInUse,
    Timeout,
    UnidentifiedException,
}
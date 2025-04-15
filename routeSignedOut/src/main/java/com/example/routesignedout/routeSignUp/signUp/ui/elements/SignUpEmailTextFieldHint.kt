package com.example.routesignedout.routeSignUp.signUp.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintOptions
import com.example.routesignedout.routeSignUp.signUp.model.SignUpEmailHintViewState
import com.example.sharedui.sharedUiElements.divider.verticalDivider5
import com.example.sharedui.R
import com.example.sharedui.hint.textFieldHint

@Composable
internal fun signUpEmailTextFieldHint(viewState: SignUpEmailHintViewState) {

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
                                    SignUpEmailHintOptions.Timeout -> stringResource(R.string.the_process_took_too_long)
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
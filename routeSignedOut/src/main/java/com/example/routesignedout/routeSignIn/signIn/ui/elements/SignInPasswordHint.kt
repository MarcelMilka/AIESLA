package com.example.routesignedout.routeSignIn.signIn.ui.elements

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
import com.example.routesignedout.routeSignIn.signIn.model.SignInPasswordHintOptions
import com.example.routesignedout.routeSignIn.signIn.model.SignInPasswordHintViewState
import com.example.sharedui.sharedUiElements.divider.verticalDivider5
import com.example.sharedui.R
import com.example.sharedui.hint.textFieldHint

@Composable
internal fun signInPasswordHint(viewState: SignInPasswordHintViewState) {

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
                                    SignInPasswordHintOptions.Timeout -> stringResource(R.string.the_process_took_too_long)
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
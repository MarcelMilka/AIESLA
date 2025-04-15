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
import com.example.authentication.credentials.PasswordRequirements
import com.example.routesignedout.routeSignUp.signUp.model.SignUpPasswordHintViewState
import com.example.sharedui.sharedUiElements.divider.verticalDivider5
import com.example.sharedui.R.string

@Composable
internal fun signUpPasswordHint(viewState: SignUpPasswordHintViewState) {

    AnimatedVisibility(
        visible = viewState is SignUpPasswordHintViewState.Visible,
        content = {

            Column(
                modifier = Modifier.width(300.dp).testTag("SignUpPasswordTextFieldHint"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    verticalDivider5()

                    when(viewState) {

                        is SignUpPasswordHintViewState.Visible -> {

                            passwordRequirement(
                                isGreen = viewState.totalCharacters.isGreen,
                                content = "${viewState.totalCharacters.currentCount}/${stringResource(string.at_least)} ${PasswordRequirements.MIN_CHARACTERS_COUNT} ${stringResource(string.sign_up)}",
                                testTag = "totalCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.uppercaseCharacters.isGreen,
                                content = "${viewState.uppercaseCharacters.currentCount}/${stringResource(string.at_least)} ${PasswordRequirements.MIN_UPPERCASE_COUNT} ${stringResource(string.uppercase_letters)}",
                                testTag = "uppercaseCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.specialCharacters.isGreen,
                                content = "${viewState.specialCharacters.currentCount}/${stringResource(string.at_least)} ${PasswordRequirements.MIN_SPECIAL_CHARACTER_COUNT} ${stringResource(string.special_character)}",
                                testTag = "specialCharacters"
                            )

                            passwordRequirement(
                                isGreen = viewState.numericCharacters.isGreen,
                                content = "${viewState.numericCharacters.currentCount}/${stringResource(string.at_least)} ${PasswordRequirements.MIN_NUMERIC_CHARACTER_COUNT} ${stringResource(string.numeric_character)}",
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
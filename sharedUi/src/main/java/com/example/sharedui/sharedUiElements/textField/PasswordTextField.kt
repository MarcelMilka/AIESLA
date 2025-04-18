package com.example.sharedui.sharedUiElements.textField

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.sharedui.R
import com.example.sharedui.sharedUiElements.label.textFieldLabel
import com.example.sharedui.theme.EmailPasswordTextFieldTextStyle
import com.example.sharedui.theme.Primary
import com.example.sharedui.dimensions.RoundedCorner.R20

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

        shape = RoundedCornerShape(R20),

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

                modifier = Modifier.testTag("PasswordTextField - icon button"),

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
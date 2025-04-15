package com.example.sharedui.sharedUiElements.textField

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.sharedui.R
import com.example.sharedui.dimensions.RoundedCorner
import com.example.sharedui.sharedUiElements.label.textFieldLabel
import com.example.sharedui.theme.EmailPasswordTextFieldTextStyle
import com.example.sharedui.theme.Primary

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

        shape = RoundedCornerShape(RoundedCorner.R20),

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
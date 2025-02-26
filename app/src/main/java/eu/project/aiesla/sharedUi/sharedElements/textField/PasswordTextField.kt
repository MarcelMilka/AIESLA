package eu.project.aiesla.sharedUi.sharedElements.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import eu.project.aiesla.R
import eu.project.aiesla.sharedUi.sharedElements.text.primaryCenteredLabel50

@Composable
fun passwordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester
) {

    TextField(

        value = value,
        onValueChange = { onValueChange(it) },

        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),

        placeholder = {

            primaryCenteredLabel50(
                content = stringResource(R.string.password),
            )
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        keyboardActions = KeyboardActions(
            onDone = {focusRequester.freeFocus()}
        ),

        textStyle = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
            disabledContainerColor = Transparent,
            cursorColor = White,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent
        ),
        visualTransformation = PasswordVisualTransformation()
    )
}
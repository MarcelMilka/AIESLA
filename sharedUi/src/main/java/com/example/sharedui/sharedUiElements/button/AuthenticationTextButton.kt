package com.example.sharedui.sharedUiElements.button

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.sharedui.sharedUiElements.label.primaryCenteredLabel

@Composable
fun authenticationTextButton(
    content: String,
    testTag: String,
    onClick: () -> Unit
) {

    TextButton(
        onClick = {onClick()},
        modifier = Modifier.testTag(testTag),
        content = {

            primaryCenteredLabel(content = content)
        }
    )
}
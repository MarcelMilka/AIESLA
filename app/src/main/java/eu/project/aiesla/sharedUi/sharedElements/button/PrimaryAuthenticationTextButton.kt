package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import eu.project.aiesla.sharedUi.sharedElements.text.bigPrimaryLabel
import eu.project.aiesla.sharedUi.sharedElements.text.primaryLabel

@Composable
fun primaryAuthenticationTextButton(
    content: String,
    testTag: String,
    onClick: () -> Unit
) {

    TextButton(
        onClick = {onClick()},
        modifier = Modifier.testTag(testTag),
        content = {

            bigPrimaryLabel(content = content)
        }
    )
}

@Composable
fun secondaryAuthenticationTextButton(
    content: String,
    testTag: String,
    onClick: () -> Unit
) {

    TextButton(
        onClick = {onClick()},
        modifier = Modifier.testTag(testTag),
        content = {

            primaryLabel(content = content)
        }
    )
}
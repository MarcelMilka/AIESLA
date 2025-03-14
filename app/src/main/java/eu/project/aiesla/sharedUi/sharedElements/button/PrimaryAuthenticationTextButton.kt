package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.aiesla.sharedConstants.RoundedCorner
import eu.project.aiesla.sharedUi.sharedElements.text.primaryCenteredLabel
import eu.project.aiesla.sharedUi.theme.Primary

@Composable
fun primaryAuthenticationButton(
    content: String,
    testTag: String,
    onClick: () -> Unit
) {

    Button(

        onClick = { onClick() },

        modifier = Modifier
            .width(250.dp)
            .height(50.dp)
            .testTag(testTag),

        shape = RoundedCornerShape(RoundedCorner.MEDIUM.dp),

        colors = ButtonDefaults.buttonColors(containerColor = Primary),

        content = {

            primaryCenteredLabel(content = content)
        }
    )
}

@Composable
fun secondaryAuthenticationTextButton(
    content: String,
    testTag: String,
    onClick: () -> Unit) {

    TextButton(
        onClick = {onClick()},
        modifier = Modifier.testTag(testTag),
        content = {

            primaryCenteredLabel(content = content)
        }
    )
}
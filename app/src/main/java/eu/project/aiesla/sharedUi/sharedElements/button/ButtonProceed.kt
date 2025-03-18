package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.sharedConstants.RoundedCorner
import eu.project.aiesla.sharedUi.sharedElements.text.secondaryCenteredLabel
import eu.project.aiesla.sharedUi.theme.Primary
import eu.project.aiesla.sharedUi.theme.PrimaryWhite

@Composable
fun buttonProceed(
    content: String,
    buttonProceedViewState: ButtonProceedViewState,
    testTag: String,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(

        targetValue = when (buttonProceedViewState) {
            ButtonProceedViewState.Disabled -> Transparent
            ButtonProceedViewState.Enabled -> Primary
            ButtonProceedViewState.Pending -> Primary
            ButtonProceedViewState.Successful -> Primary
            ButtonProceedViewState.Unsuccessful -> Transparent
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
    )


    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(40.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(RoundedCorner.FULLY_ROUNDED),

        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = PrimaryWhite,
            disabledContainerColor = containerColor,
            disabledContentColor = PrimaryWhite
        ),

        enabled = buttonProceedViewState is ButtonProceedViewState.Enabled,

        content = {

            when (buttonProceedViewState) {

                ButtonProceedViewState.Disabled -> {
                    secondaryCenteredLabel(content = content)
                }

                ButtonProceedViewState.Enabled -> {
                    secondaryCenteredLabel(content = content)
                }

                ButtonProceedViewState.Pending -> {
                    secondaryCenteredLabel(content = "...")
                }

                ButtonProceedViewState.Successful -> {
                    Icon(
                        painter = painterResource(R.drawable.check),
                        tint = PrimaryWhite,
                        contentDescription = "icon 'check'",
                    )
                }

                ButtonProceedViewState.Unsuccessful -> {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        tint = PrimaryWhite,
                        contentDescription = "icon 'close'",
                    )
                }
            }
        }
    )
}

sealed class ButtonProceedViewState {
    data object Disabled: ButtonProceedViewState()
    data object Enabled: ButtonProceedViewState()
    data object Pending: ButtonProceedViewState()
    data object Successful: ButtonProceedViewState()
    data object Unsuccessful: ButtonProceedViewState()
}
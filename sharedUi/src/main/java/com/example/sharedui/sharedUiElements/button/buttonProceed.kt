package com.example.sharedui.sharedUiElements.button

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sharedui.theme.PrimaryWhite
import com.example.sharedui.R
import com.example.sharedui.dimensions.RoundedCorner
import com.example.sharedui.sharedUiElements.label.primaryCenteredLabel
import com.example.sharedui.theme.Primary

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
        shape = RoundedCornerShape(RoundedCorner.fullyRounded),

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
                    primaryCenteredLabel(content = content)
                }

                ButtonProceedViewState.Enabled -> {
                    primaryCenteredLabel(content = content)
                }

                ButtonProceedViewState.Pending -> {
                    primaryCenteredLabel(content = "...")
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
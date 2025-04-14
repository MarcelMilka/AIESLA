package com.example.sharedui.sharedUiElements.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.sharedui.dimensions.RoundedCorner
import com.example.sharedui.sharedUiElements.label.primaryCenteredLabel
import com.example.sharedui.theme.Primary

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

        shape = RoundedCornerShape(RoundedCorner.R20),

        colors = ButtonDefaults.buttonColors(containerColor = Primary),

        content = {

            primaryCenteredLabel(content = content)
        }
    )
}
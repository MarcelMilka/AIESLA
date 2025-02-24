package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.project.aiesla.sharedUi.sharedElements.text.bigPrimaryLabel
import eu.project.aiesla.sharedUi.theme.DarkPrimary
import eu.project.aiesla.sharedUi.theme.DarkSecondary

@Composable fun bigPrimaryAuthenticationOptionButton(
    content: String,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(0.8f),
        enabled = true,
        shape = RoundedCornerShape(size = 50.dp),
        colors = ButtonColors(
            containerColor = DarkPrimary,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        content = {bigPrimaryLabel(content = content)}
    )
}

@Composable fun smallSecondaAuthenticationOptionButton(
    content: String,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(0.8f),
        enabled = true,
        shape = RoundedCornerShape(size = 50.dp),
        colors = ButtonColors(
            containerColor = DarkSecondary,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        content = {bigPrimaryLabel(content = content)}
    )
}
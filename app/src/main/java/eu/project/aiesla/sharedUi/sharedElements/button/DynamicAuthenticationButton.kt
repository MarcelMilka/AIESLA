package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.aiesla.sharedConstants.RoundedCorner
import eu.project.aiesla.sharedUi.sharedElements.text.secondaryCenteredLabel
import eu.project.aiesla.sharedUi.theme.Primary
import eu.project.aiesla.sharedUi.theme.PrimaryWhite
import eu.project.aiesla.sharedUi.theme.SecondaryWhite

@Composable
fun buttonSignInSignUp(content: String, testTag: String, enabled: Boolean, onClick: () -> Unit ) {

    val containerColor by animateColorAsState(
        targetValue = if (enabled) Primary else Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = ""
    )

    Button(

        onClick = { onClick() },

        modifier = Modifier
            .width(200.dp)
            .height(40.dp)
            .testTag(testTag),

        shape = RoundedCornerShape(RoundedCorner.FULLY_ROUNDED),

        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = PrimaryWhite,
            disabledContentColor = SecondaryWhite
        ),

        enabled = enabled,

        content = { secondaryCenteredLabel(content = content) }
    )
}
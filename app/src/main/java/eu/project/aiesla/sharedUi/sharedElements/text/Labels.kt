package eu.project.aiesla.sharedUi.sharedElements.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import eu.project.aiesla.sharedConstants.TextSize
import eu.project.aiesla.sharedUi.theme.PrimaryWhite
import eu.project.aiesla.sharedUi.theme.SecondaryWhite
import eu.project.aiesla.sharedUi.theme.quickSandBold
import eu.project.aiesla.sharedUi.theme.quickSandMedium

@Composable
fun bigPrimaryLabel(content: String) {

    Text(
        text = content,
        color = White,
        fontSize = 24.sp,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Center,
        softWrap = true,
        modifier = Modifier.testTag("bigPrimaryLabel")
    )
}

@Composable
fun primaryCenteredLabel(content: String) {

    Text(
        text = content,
        color = PrimaryWhite,
        fontSize = TextSize.PRIMARY_LABEL.sp,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Center,
        softWrap = true
    )
}

@Composable
fun secondaryCenteredLabel(content: String) {

    Text(
        text = content,
        fontSize = TextSize.SECONDARY_LABEL.sp,
        fontFamily = quickSandBold,
        textAlign = TextAlign.Center,
        softWrap = true
    )
}

@Composable
fun textFieldLabel(content: String) {

    Text(
        text = content,
        color = SecondaryWhite,
        fontSize = TextSize.PRIMARY_LABEL.sp,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Start,
        softWrap = true
    )
}
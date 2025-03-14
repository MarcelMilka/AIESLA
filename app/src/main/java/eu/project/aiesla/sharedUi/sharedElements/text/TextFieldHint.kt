package eu.project.aiesla.sharedUi.sharedElements.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import eu.project.aiesla.sharedConstants.TextSize
import eu.project.aiesla.sharedUi.theme.SecondaryWhite
import eu.project.aiesla.sharedUi.theme.quickSandMedium

@Composable
fun textFieldHint(content: String, testTag: String) {

    Text(
        text = content,
        fontSize = TextSize.TEXT_FIELD_HINT.sp,
        color = SecondaryWhite,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier.testTag(testTag)
    )
}
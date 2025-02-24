package eu.project.aiesla.sharedUi.sharedElements.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import eu.project.aiesla.sharedUi.theme.quickSandMedium

@Composable
fun bigPrimaryLabel(content: String) {

    Text(
        text = content,
        color = White,
        fontSize = 24.sp,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Center,
        softWrap = true
    )
}

@Composable
fun primaryLabel(content: String) {

    Text(
        text = content,
        color = White,
        fontSize = 16.sp,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Center,
        softWrap = true
    )
}
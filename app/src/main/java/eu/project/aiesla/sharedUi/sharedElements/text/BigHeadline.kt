package eu.project.aiesla.sharedUi.sharedElements.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import eu.project.aiesla.sharedUi.theme.quickSandMedium

@Composable
fun bigHeadline(content: String) {

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Text(
                text = content,
                color = White,
                fontSize = 26.sp,
                modifier = Modifier.fillMaxWidth(0.9f),
                fontFamily = quickSandMedium,
                textAlign = TextAlign.Center,
                softWrap = true
            )
        }
    )
}
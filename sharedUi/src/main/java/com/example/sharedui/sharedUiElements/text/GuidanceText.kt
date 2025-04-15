package com.example.sharedui.sharedUiElements.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.example.sharedui.dimensions.TextSize
import com.example.sharedui.theme.quickSandMedium

@Composable
fun guidanceText(content: String) {

    Text(
        text = content,
        color = White,
        fontSize = TextSize.guidance,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Center,
        softWrap = true,
        modifier = Modifier.testTag("guidanceText")
    )
}
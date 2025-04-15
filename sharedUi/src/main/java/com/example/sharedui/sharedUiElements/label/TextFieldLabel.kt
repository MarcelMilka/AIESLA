package com.example.sharedui.sharedUiElements.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.example.sharedui.dimensions.TextSize
import com.example.sharedui.theme.SecondaryWhite
import com.example.sharedui.theme.quickSandMedium

@Composable
fun textFieldLabel(content: String) {

    Text(
        text = content,
        color = SecondaryWhite,
        fontSize = TextSize.primary,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Start,
        softWrap = true
    )
}
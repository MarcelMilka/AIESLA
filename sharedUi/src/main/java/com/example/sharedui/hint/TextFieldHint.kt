package com.example.sharedui.hint

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.example.sharedui.dimensions.TextSize
import com.example.sharedui.theme.SecondaryWhite
import com.example.sharedui.theme.quickSandMedium

@Composable
fun textFieldHint(content: String, testTag: String) {

    Text(
        text = content,
        fontSize = TextSize.textFieldHint,
        color = SecondaryWhite,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier.testTag(testTag)
    )
}
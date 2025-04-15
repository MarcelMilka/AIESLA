package com.example.routesignedout.routeSignUp.signUp.ui.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.example.sharedui.dimensions.TextSize
import com.example.sharedui.theme.quickSandMedium

@Composable
internal fun dynamicTextFieldHint(content: String, color: Color, testTag: String) {

    Text(
        text = content,
        fontSize = TextSize.textFieldHint,
        color = color,
        fontFamily = quickSandMedium,
        textAlign = TextAlign.Start,
        modifier = Modifier.testTag(testTag)
    )
}
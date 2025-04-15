package com.example.routesignedout.routeSignUp.signUp.ui.elements

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.Green
import com.example.sharedui.theme.SecondaryWhite

@Composable
internal fun passwordRequirement(isGreen: Boolean, content: String, testTag: String) {

    var targetColor by remember { mutableStateOf(SecondaryWhite) }

    targetColor = when (isGreen) {
        true -> Green
        false -> SecondaryWhite
    }

    val color by animateColorAsState(targetValue = targetColor)

    dynamicTextFieldHint(
        content = content,
        color = color,
        testTag = testTag
    )
}
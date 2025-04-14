package com.example.sharedui.sharedUiElements.divider

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sharedui.dimensions.Space

@Composable
fun verticalDivider20() {

    Spacer(modifier = Modifier.height(height = Space.S20))
}

@Composable
fun verticalDivider10() {

    Spacer(modifier = Modifier.height(height = Space.S10))

}

@Composable
fun verticalDivider5() {

    Spacer(modifier = Modifier.height(height = Space.S5))
}
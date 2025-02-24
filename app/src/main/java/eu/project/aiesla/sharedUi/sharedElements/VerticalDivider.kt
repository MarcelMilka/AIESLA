package eu.project.aiesla.sharedUi.sharedElements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.project.aiesla.sharedConstants.Space

@Composable
fun verticalDividerM() {

    Spacer(
        modifier = Modifier
            .height(height = Space.S20.dp)
    )
}

@Composable
fun verticalDividerS() {

    Spacer(
        modifier = Modifier
            .height(height = Space.S5.dp)
    )
}
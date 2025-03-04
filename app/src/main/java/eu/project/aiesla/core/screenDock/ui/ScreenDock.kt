package eu.project.aiesla.core.screenDock.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.aiesla.core.screenDock.model.ScreenDockViewState
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedConstants.navigation.CurrentScreen
import eu.project.aiesla.sharedUi.theme.White50

@Composable
fun screenDock(
    screenDockViewState: ScreenDockViewState,
    onNavigatePodcastsScreen: () -> Unit,
    onNavigateHomeScreen: () -> Unit,
    onNavigateStudyScreen: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = Padding.P20.dp, bottom = Padding.P20.dp, end = Padding.P20.dp)
            .testTag("screenDock"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            when (screenDockViewState) {

                ScreenDockViewState.Invisible -> {}

                is ScreenDockViewState.Visible -> {

                    HorizontalDivider(
                        thickness = 0.25.dp,
                        color = White50
                    )

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {

                            screenDockButton(
                                icon = Icons.Rounded.Home,
                                isActive = screenDockViewState.currentScreen == CurrentScreen.HomeScreen,
                                testTag = "screenDockButton 'home'",
                                onClick = {onNavigateHomeScreen()}
                            )

                            screenDockButton(
                                icon = Icons.Rounded.Build,
                                isActive = screenDockViewState.currentScreen == CurrentScreen.StudyScreen,
                                testTag = "screenDockButton 'study'",
                                onClick = {onNavigateStudyScreen()}
                            )
                        }
                    )
                }

                ScreenDockViewState.Error -> {

                    HorizontalDivider(
                        thickness = 0.25.dp,
                        color = White50
                    )
                }
            }
        }
    )
}

@Composable
private fun screenDockButton(
    icon: ImageVector,
    isActive: Boolean,
    testTag: String,
    onClick: () -> Unit
) {

    val targetSize =
        when(isActive) {
            true -> {
                35
            }
            false -> {
                30
            }
        }

    val size by animateIntAsState(
        targetValue = targetSize,
        animationSpec = SpringSpec(
            stiffness = Spring.StiffnessVeryLow,
            dampingRatio = Spring.DampingRatioNoBouncy
        )
    )

    val targetTint =
        when(isActive) {
            true -> {
                White
            }
            false -> {
                White50
            }
        }

    val tint by animateColorAsState(
        targetValue = targetTint,
        animationSpec = SpringSpec(
            stiffness = Spring.StiffnessVeryLow,
            dampingRatio = Spring.DampingRatioNoBouncy
        )
    )

    IconButton(

        onClick = {onClick()},
        modifier = Modifier.testTag(testTag),
        enabled = !isActive,
        content = {
            Icon(
                modifier = Modifier.size(size.dp),
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
    )
}
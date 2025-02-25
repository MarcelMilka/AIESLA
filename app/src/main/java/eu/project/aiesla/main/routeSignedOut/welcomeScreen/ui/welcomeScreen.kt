package eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.aiesla.R
import eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui.segments.welcomeScreenLowerSegment
import eu.project.aiesla.sharedConstants.Padding
import eu.project.aiesla.sharedUi.sharedElements.text.bigHeadline
import eu.project.aiesla.sharedUi.theme.DarkBackground

@Composable
fun welcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(Padding.P20.dp),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            bigHeadline(content = stringResource(R.string.welcome_title))

            welcomeScreenLowerSegment(
                onSignIn = { onSignIn() },
                onSignUp = { onSignUp() },
                onContinueWithoutAccount = { onContinueWithoutAccount() }
            )
        }
    )
}
package eu.project.aiesla.main.routeSignedOut.welcomeScreen.ui.segments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.project.aiesla.R
import eu.project.aiesla.sharedUi.sharedElements.button.bigPrimaryAuthenticationOptionButton
import eu.project.aiesla.sharedUi.sharedElements.button.smallSecondaAuthenticationOptionButton
import eu.project.aiesla.sharedUi.sharedElements.verticalDividerS

@Composable
fun welcomeScreenLowerSegment(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            bigPrimaryAuthenticationOptionButton(
                content = stringResource(R.string.sign_in)
            ) {
                onSignIn()
            }

            verticalDividerS()

            bigPrimaryAuthenticationOptionButton(
                content = stringResource(R.string.sign_up)
            ) {
                onSignUp()
            }

            verticalDividerS()

            smallSecondaAuthenticationOptionButton(
                content = stringResource(R.string.continue_without_account)
            ) {
                onContinueWithoutAccount()
            }
        }
    )
}
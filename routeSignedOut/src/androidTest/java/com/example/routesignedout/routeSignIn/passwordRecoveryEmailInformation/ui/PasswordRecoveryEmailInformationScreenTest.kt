package com.example.routesignedout.routeSignIn.passwordRecoveryEmailInformation.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.sharedui.R

class PasswordRecoveryEmailInformationScreenTest {

    @get:Rule(order = 0)
    val ctr = createComposeRule()

    // SignInScreen
    private val passwordRecoveryEmailInformationScreen = ctr.onNodeWithTag("PasswordRecoveryEmailInformationScreen")
    private val checkYourEmailInformation = ctr.onNodeWithText(getResourceString(R.string.check_your_email))
    private val buttonSignIn = ctr.onNodeWithTag("PasswordRecoveryEmailInformationScreen primaryAuthenticationTextButton 'Sign in.'")


    private var onSignIn = false

    @Before
    fun before() {

        onSignIn = false

        ctr.setContent {

            passwordRecoveryEmailInformationScreen(
                onSignIn = { onSignIn = true }
            )
        }
    }

    @Test
    fun `make sure all default elements are displayed`() = runTest {

        passwordRecoveryEmailInformationScreen.assertIsDisplayed()
        checkYourEmailInformation.assertIsDisplayed()
        buttonSignIn.assertIsDisplayed()
    }

    @Test
    fun `buttonSignIn works as intended`() = runTest {

        buttonSignIn.performClick()
        assertTrue(onSignIn)
    }

}

private fun getResourceString(id: Int): String {
    val targetContext: Context = InstrumentationRegistry.getTargetContext()
    return targetContext.resources.getString(id)
}
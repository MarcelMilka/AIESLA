package com.example.routesignedout.routeSignUp.signUpEmailInformation.ui

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.sharedui.R

class SignUpEmailInformationScreenTest {

    @get:Rule(order = 0)
    val ctr = createComposeRule()

    // SignUpEmailInformationScreen
    private val signUpEmailInformationScreen = ctr.onNodeWithTag("SignUpEmailInformationScreen")
    private val checkYourEmailInformation = ctr.onNodeWithText(getResourceString(R.string.verify_your_email))
    private val buttonSignIn = ctr.onNodeWithTag("SignUpEmailInformationScreen - primaryAuthenticationButton 'Sign in.'")

    private var onSignIn = false

    @Before
    fun before() {

        onSignIn = false

        ctr.setContent {

            signUpEmailInformationScreen(
                onSignIn = { onSignIn = true }
            )
        }
    }

    @Test
    fun `make sure all default elements are displayed`() = runTest {

        signUpEmailInformationScreen.assertIsDisplayed()
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
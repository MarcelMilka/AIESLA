package eu.project.aiesla.sharedUi.sharedElements.button

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonProceedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `buttonProceed is disabled`() {

        composeTestRule.setContent {
            buttonProceed(
                content = "Proceed",
                buttonProceedViewState = ButtonProceedViewState.Disabled,
                testTag = "proceedButton",
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag("proceedButton").assertIsNotEnabled()
    }

    @Test
    fun `buttonProceed is enabled`() = runTest {

        var clicked = false

        composeTestRule.setContent {
            buttonProceed(
                content = "Proceed",
                buttonProceedViewState = ButtonProceedViewState.Enabled,
                testTag = "proceedButton",
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("proceedButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("proceedButton").performClick()
        composeTestRule.awaitIdle()
        assertTrue(clicked)
    }

    @Test
    fun `buttonProceed is pending`() {
        composeTestRule.setContent {
            buttonProceed(
                content = "Proceed",
                buttonProceedViewState = ButtonProceedViewState.Pending,
                testTag = "proceedButton",
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag("proceedButton").assertIsNotEnabled()
        composeTestRule.onNodeWithText("...").assertIsDisplayed()
    }

    @Test
    fun `buttonProceed is successful`() {
        composeTestRule.setContent {
            buttonProceed(
                content = "Proceed",
                buttonProceedViewState = ButtonProceedViewState.Successful,
                testTag = "proceedButton",
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag("proceedButton").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("icon 'check'").assertIsDisplayed()
    }

    @Test
    fun `buttonProceed is unsuccessful`() {
        composeTestRule.setContent {
            buttonProceed(
                content = "Proceed",
                buttonProceedViewState = ButtonProceedViewState.Unsuccessful,
                testTag = "proceedButton",
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag("proceedButton").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("icon 'close'").assertIsDisplayed()
    }
}
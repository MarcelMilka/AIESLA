package eu.project.aiesla.core.screenDock.vm

import app.cash.turbine.test
import eu.project.aiesla.core.screenDock.model.ScreenDockViewState
import eu.project.aiesla.sharedConstants.navigation.CurrentMainRoute
import eu.project.aiesla.sharedConstants.navigation.CurrentScreen
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(DelicateCoroutinesApi::class)
class ScreenDockViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var screenDockViewModel: ScreenDockViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        screenDockViewModel = ScreenDockViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `ScreenDockViewState is set to Invisible by default`() {

        assertEquals(
            ScreenDockViewState.Invisible,
            screenDockViewModel.screenDockViewState.value
        )
    }

    @Test
    fun `updateViewState works properly`() = runTest {

        screenDockViewModel.screenDockViewState.test {

            // 1: Invisible by default
            assertEquals(ScreenDockViewState.Invisible, this.awaitItem())

            // 2: Visible, HomeScreen
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.HomeScreen)
            assertEquals(ScreenDockViewState.Visible(CurrentScreen.HomeScreen), this.awaitItem())

            // 3: Visible, StudyScreen
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.StudyScreen)
            assertEquals(ScreenDockViewState.Visible(CurrentScreen.StudyScreen), awaitItem())
        }
    }

    @Test
    fun `Error is set when main route is Error`() = runTest {

        screenDockViewModel.screenDockViewState.test {

            // 1: Invisible by default
            assertEquals(ScreenDockViewState.Invisible, this.awaitItem())

            // 2: Visible, HomeScreen
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.HomeScreen)
            assertEquals(ScreenDockViewState.Visible(CurrentScreen.HomeScreen), this.awaitItem())

            // 3: Error
            screenDockViewModel.updateViewState(CurrentMainRoute.Error, CurrentScreen.StudyScreen)
            assertEquals(ScreenDockViewState.Error, awaitItem())
        }
    }

    @Test
    fun `Error is set when screen is Error`() = runTest {

        screenDockViewModel.screenDockViewState.test {

            // 1: Invisible by default
            assertEquals(ScreenDockViewState.Invisible, this.awaitItem())

            // 2: Visible, HomeScreen
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.HomeScreen)
            assertEquals(ScreenDockViewState.Visible(CurrentScreen.HomeScreen), this.awaitItem())

            // 3: Error
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.Error)
            assertEquals(ScreenDockViewState.Error, awaitItem())
        }
    }

    @Test
    fun `Error is set when both main route and screen are Error`() = runTest {

        screenDockViewModel.screenDockViewState.test {

            // 1: Invisible by default
            assertEquals(ScreenDockViewState.Invisible, this.awaitItem())

            // 2: Visible, HomeScreen
            screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn, CurrentScreen.HomeScreen)
            assertEquals(ScreenDockViewState.Visible(CurrentScreen.HomeScreen), this.awaitItem())

            // 3: Error
            screenDockViewModel.updateViewState(CurrentMainRoute.Error, CurrentScreen.Error)
            assertEquals(ScreenDockViewState.Error, awaitItem())
        }
    }
}
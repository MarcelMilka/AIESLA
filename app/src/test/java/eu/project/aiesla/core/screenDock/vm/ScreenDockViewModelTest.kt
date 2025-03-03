package eu.project.aiesla.core.screenDock.vm

import eu.project.aiesla.core.screenDock.model.ScreenDockViewState
import eu.project.aiesla.sharedConstants.navigation.CurrentMainRoute
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.advanceUntilIdle
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateViewState works properly`() = runTest {

        //
        assertEquals(
            ScreenDockViewState.Invisible,
            screenDockViewModel.screenDockViewState.value
        )

        //
        screenDockViewModel.updateViewState(CurrentMainRoute.SignedIn)
        advanceUntilIdle()
        assertEquals(
            ScreenDockViewState.Visible,
            screenDockViewModel.screenDockViewState.value
        )

        //
        screenDockViewModel.updateViewState(CurrentMainRoute.RouteIsUnknown)
        advanceUntilIdle()
        assertEquals(
            ScreenDockViewState.Visible,
            screenDockViewModel.screenDockViewState.value
        )

        //
        screenDockViewModel.updateViewState(CurrentMainRoute.RouteIsNull)
        advanceUntilIdle()
        assertEquals(
            ScreenDockViewState.Visible,
            screenDockViewModel.screenDockViewState.value
        )
    }
}
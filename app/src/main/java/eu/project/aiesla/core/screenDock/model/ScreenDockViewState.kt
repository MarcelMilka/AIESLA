package eu.project.aiesla.core.screenDock.model

import eu.project.aiesla.sharedConstants.navigation.CurrentScreen

sealed class ScreenDockViewState {
    data class Visible(var currentScreen: CurrentScreen): ScreenDockViewState()
    data object Invisible: ScreenDockViewState()
    data object Error: ScreenDockViewState()
}
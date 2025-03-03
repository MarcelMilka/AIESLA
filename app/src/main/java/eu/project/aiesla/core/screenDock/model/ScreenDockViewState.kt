package eu.project.aiesla.core.screenDock.model

sealed class ScreenDockViewState {

    data object Visible: ScreenDockViewState()
    data object Invisible: ScreenDockViewState()
}
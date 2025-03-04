package eu.project.aiesla.core.screenDock.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.project.aiesla.core.screenDock.model.ScreenDockViewState
import eu.project.aiesla.sharedConstants.navigation.CurrentMainRoute
import eu.project.aiesla.sharedConstants.navigation.CurrentScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenDockViewModel @Inject constructor(): ViewModel() {

    private var _screenDockViewState = MutableStateFlow<ScreenDockViewState>(ScreenDockViewState.Invisible)
    val screenDockViewState = _screenDockViewState.asStateFlow()

    fun updateViewState(currentMainRoute: CurrentMainRoute, currentScreen: CurrentScreen) {

        viewModelScope.launch {

            _screenDockViewState.emit(
                if (currentMainRoute == CurrentMainRoute.Error || currentScreen == CurrentScreen.Error) {
                    ScreenDockViewState.Error
                }
                else {
                    when {
                        currentMainRoute == CurrentMainRoute.SignedIn -> ScreenDockViewState.Visible(currentScreen)
                        else -> ScreenDockViewState.Invisible
                    }
                }
            )
        }
    }
}
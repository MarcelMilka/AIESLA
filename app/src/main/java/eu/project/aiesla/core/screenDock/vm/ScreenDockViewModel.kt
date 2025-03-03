package eu.project.aiesla.core.screenDock.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.project.aiesla.core.screenDock.model.ScreenDockViewState
import eu.project.aiesla.sharedConstants.navigation.CurrentMainRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenDockViewModel: ViewModel() {

    private var _screenDockViewState = MutableStateFlow<ScreenDockViewState>(ScreenDockViewState.Invisible)
    val screenDockViewState = _screenDockViewState.asStateFlow()

    fun updateViewState(currentMainRoute: CurrentMainRoute) {

        viewModelScope.launch {

            _screenDockViewState.emit(
                value =
                    if (currentMainRoute == CurrentMainRoute.SignedOut) { ScreenDockViewState.Invisible }
                    else {ScreenDockViewState.Visible}
            )
        }
    }
}
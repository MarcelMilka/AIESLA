package eu.project.aiesla.core.routeSignedIn.routeStudyScreen.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.aiesla.core.routeSignedIn.routeStudyScreen.data.StudyRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(private val studyRepository: StudyRepository): ViewModel() {

    init {

        viewModelScope.launch {

            studyRepository.x().also { Log.d("Halla!", "$it") }
        }
    }
}
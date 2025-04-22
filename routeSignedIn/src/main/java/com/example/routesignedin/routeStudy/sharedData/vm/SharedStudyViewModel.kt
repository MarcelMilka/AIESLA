package com.example.routesignedin.routeStudy.sharedData.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routesignedin.routeStudy.sharedData.data.SharedStudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SharedStudyViewModel @Inject constructor(
    val sharedStudyRepository: SharedStudyRepository
): ViewModel() {

    init {

        this.viewModelScope
            .launch { sharedStudyRepository.checkIfLocalDatabaseMustBeInitialized() }
    }
}
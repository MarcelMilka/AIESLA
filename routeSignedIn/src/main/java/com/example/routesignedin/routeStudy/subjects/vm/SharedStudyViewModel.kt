package com.example.routesignedin.routeStudy.subjects.vm

import androidx.lifecycle.ViewModel
import com.example.routesignedin.routeStudy.subjects.data.SharedStudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedStudyViewModel @Inject constructor(
    val sharedStudyRepository: SharedStudyRepository
): ViewModel()
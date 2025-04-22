package com.example.routesignedin.routeStudy.sharedData.vm

import androidx.lifecycle.ViewModel
import com.example.routesignedin.routeStudy.sharedData.data.SharedStudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SharedStudyViewModel @Inject constructor(
    val sharedStudyRepository: SharedStudyRepository
): ViewModel()
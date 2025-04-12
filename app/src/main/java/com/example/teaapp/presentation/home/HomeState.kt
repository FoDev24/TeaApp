package com.example.teaapp.presentation.home

import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.Competition

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val areas: List<Area> = emptyList(),
    val competitionsByArea: Map<Int, List<Competition>> = emptyMap(),
    val isOffline: Boolean = false
)
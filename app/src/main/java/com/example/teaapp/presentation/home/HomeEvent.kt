package com.example.teaapp.presentation.home

import android.content.Context

sealed class HomeEvent {
    object LoadAreas : HomeEvent()
    data class LoadCompetitions(val areaId: Int) : HomeEvent()
    data class OnNetworkChanged(val isConnected: Boolean) : HomeEvent()

}
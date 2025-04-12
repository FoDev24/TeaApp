package com.example.teaapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.domain.repository.FootballRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.teaapp.data.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Home screen that handles UI events and state using the MVI pattern.
 *
 * Responsibilities:
 * - Fetch areas and competitions from remote API or local cache.
 * - React to network changes and update state accordingly.
 * - Maintain a reactive and immutable state exposed via [StateFlow].
 */
@HiltViewModel
class HomeVm @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState())
    val state: StateFlow<HomeState> = _state



    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadAreas -> loadAreas()
            is HomeEvent.LoadCompetitions -> loadCompetitions(event.areaId)
            is HomeEvent.OnNetworkChanged -> {
                _state.update { it.copy(isOffline = !event.isConnected) }
            }
        }
    }

    private fun loadAreas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            if (!_state.value.isOffline) {
                when (val result = repository.getAreasFromApi()) {
                    is Result.Success -> {
                        Log.d("VM", "API Success: ${result.data}")
                        _state.value=_state.value.copy(
                                areas = result.data ?: emptyList(),
                                isLoading = false,
                                isOffline = false
                        )
                        Log.d("VM", "API Success State: ${_state.value.areas}")

                    }

                    is Result.Error -> {
                        Log.d("VM", "API Error: ${result.message}")
                        val cached = repository.getCachedAreas()
                        Log.d("VM", "Fallback cache: $cached")
                        if (cached.isNotEmpty()) {
                            _state.update {
                                it.copy(
                                    areas = cached,
                                    isLoading = false,
                                    error = "Showing cached data due to: ${result.message}",
                                    isOffline = true
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No internet and no cached data",
                                    isOffline = true
                                )
                            }
                        }
                    }

                    else -> {
                        Log.d("VM", "Unexpected result")
                    }
                }
            } else {
                Log.d("VM", "Offline mode. Trying cache...")
                val cached = repository.getCachedAreas()
                Log.d("VM", "Cache: $cached")
                if (cached.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            areas = cached,
                            isLoading = false,
                            isOffline = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No internet and no cached data",
                            isOffline = true
                        )
                    }
                }
            }
        }
    }





    private fun loadCompetitions(areaId: Int) {
        viewModelScope.launch {
            when (val result = repository.getCompetitionsByAreaFromAPi(areaId)) {
                is Result.Success -> {
                    val updatedMap = _state.value.competitionsByArea.toMutableMap()
                    result.data?.groupBy { it.area.id }?.forEach { (actualAreaId, comps) ->
                        updatedMap[actualAreaId] = comps
                    }
                    _state.update { it.copy(competitionsByArea = updatedMap) }
                }

                is Result.Error -> {
                    val fallback = repository.getCachedCompetitions(areaId)
                    if (fallback.isNotEmpty()) {
                        val updatedMap = _state.value.competitionsByArea.toMutableMap()
                        fallback.groupBy { it.area.id }.forEach { (actualAreaId, comps) ->
                            updatedMap[actualAreaId] = comps
                        }
                        _state.update {
                            it.copy(
                                competitionsByArea = updatedMap,
                                error = "Showing cached competitions due to: ${result.message}",
                                isOffline = true
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                error = "No internet and no cached competitions available.",
                                isOffline = true
                            )
                        }
                    }
                }

                else -> Unit
            }
        }
    }



}

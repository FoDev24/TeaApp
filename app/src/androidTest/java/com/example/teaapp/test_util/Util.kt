package com.example.teaapp.test_util

import com.example.teaapp.presentation.home.HomeState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

suspend fun waitForState(
    flow: StateFlow<HomeState>,
    condition: (HomeState) -> Boolean
): HomeState {
    return flow.first { condition(it) }
}

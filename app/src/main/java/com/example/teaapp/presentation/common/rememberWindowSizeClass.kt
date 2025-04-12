package com.example.teaapp.presentation.common

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator

/**
 * Calculates and remembers the current [WindowSizeClass] for responsive layout design.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val context = LocalContext.current
    val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context as Activity)

    val density = LocalDensity.current
    val widthDp = with(density) { windowMetrics.bounds.width().toDp() }
    val heightDp = with(density) { windowMetrics.bounds.height().toDp() }

    return WindowSizeClass.calculateFromSize(DpSize(widthDp, heightDp))
}

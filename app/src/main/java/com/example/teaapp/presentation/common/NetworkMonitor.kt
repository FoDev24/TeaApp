package com.example.teaapp.presentation.common

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.teaapp.data.util.NetworkChangeReceiver

/**
 * A Composable that observes network connectivity changes using a BroadcastReceiver.
 * Automatically unregisters the receiver when the Composable leaves the composition.
 */
@Composable
fun NetworkMonitor(
    onConnectionChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val currentCallback = rememberUpdatedState(onConnectionChanged)

    DisposableEffect(Unit) {
        val receiver = NetworkChangeReceiver { isConnected ->
            currentCallback.value(isConnected)
        }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, filter)

        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Log.w("NetworkReceiver", "Receiver already unregistered")
            }
        }
    }
}

package com.example.teaapp.presentation.home

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teaapp.R
import com.example.teaapp.data.util.NetworkChangeReceiver
import com.example.teaapp.presentation.common.NetworkMonitor
import com.example.teaapp.presentation.home.component.EmptyView
import com.example.teaapp.presentation.home.component.HomeExpandableList
import com.example.teaapp.presentation.navigator.Routes
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen(
    homeViewModel: HomeVm = hiltViewModel(),
    navigate: (String) -> Unit,
    navBack: () -> Unit,
) {
    val state by homeViewModel.state.collectAsState()
    val context = LocalContext.current


    NetworkMonitor { isConnected ->
        homeViewModel.onEvent(HomeEvent.OnNetworkChanged(isConnected))
    }

    LaunchedEffect(state.isOffline) {
        if (!state.isOffline && state.areas.isEmpty()) {
            homeViewModel.onEvent(HomeEvent.LoadAreas)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        if (state.isOffline) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("You're offline.", color = Color.Black)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null && state.areas.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyView(
                            icon = painterResource(id = R.drawable.ic_empty),
                            text = "No available data."
                        )
                    }
                }


                else -> {
                    HomeExpandableList(
                        modifier = Modifier.fillMaxSize(),
                        areas = state.areas,
                        competitionsByArea = state.competitionsByArea,
                        onAreaExpanded = { areaId ->
                            homeViewModel.onEvent(HomeEvent.LoadCompetitions(areaId))
                        },
                        onCompetitionClick = { competition ->
                            val json = Uri.encode(Json.encodeToString(competition))
                            navigate("${Routes.Details.route}/$json")
                        }
                    )
                }
            }
        }

    }
}

package com.example.teaapp.presentation.navigator

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.teaapp.domain.model.Competition
import com.example.teaapp.presentation.details.CompetitionDetails
import com.example.teaapp.presentation.home.HomeScreen
import kotlinx.serialization.json.Json

/**
 * Composable responsible for handling app navigation using [NavHost].
 *
 * This function defines the navigation graph for the application,
 * mapping each route to its corresponding composable screen.
 */

@Composable
fun Navigator(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                navigate = { navController.navigate(it) },
                navBack = { navController.navigateUp() }
            )
        }

        composable(
            route = "${Routes.Details.route}/{competitionJson}",
            arguments = listOf(navArgument("competitionJson") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val competitionJson = backStackEntry.arguments?.getString("competitionJson")!!
            val competition = remember {
                Json.decodeFromString<Competition>(competitionJson)
            }
            CompetitionDetails(competition = competition, navBack = {navController.navigateUp()})
        }
    }
}

package com.example.teaapp.presentation.navigator

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Details : Routes("details")
}
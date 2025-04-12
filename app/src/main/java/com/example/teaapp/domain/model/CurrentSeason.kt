package com.example.teaapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentSeason(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val currentMatchday: Int?,
    val winner: String? = null
)
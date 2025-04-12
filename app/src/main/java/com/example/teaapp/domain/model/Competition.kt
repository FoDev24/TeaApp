package com.example.teaapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "competitions")
data class Competition(
    @PrimaryKey val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblem: String?,
    val plan: String,
    val area: Area,
    val currentSeason: CurrentSeason? = null,
    val numberOfAvailableSeasons: Int,
    val lastUpdated: String,
    val areaId: Int
)
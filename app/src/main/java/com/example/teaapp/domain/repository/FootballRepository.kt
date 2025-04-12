package com.example.teaapp.domain.repository

import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.Competition
import com.example.teaapp.data.util.Result

interface FootballRepository {
    suspend fun getAreasFromApi(): Result<List<Area>>
    suspend fun getCompetitionsByAreaFromAPi(areaId: Int): Result<List<Competition>>
    suspend fun getCachedAreas(): List<Area>
    suspend fun getCachedCompetitions(areaId: Int): List<Competition>

}
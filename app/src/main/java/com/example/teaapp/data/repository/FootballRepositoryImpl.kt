package com.example.teaapp.data.repository

import android.util.Log
import com.example.teaapp.data.local.AreaDao
import com.example.teaapp.data.local.CompetitionDao
import com.example.teaapp.data.remote.ApiService
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.Competition
import com.example.teaapp.domain.repository.FootballRepository
import com.example.teaapp.data.util.Result
import javax.inject.Inject

class FootballRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val areaDao: AreaDao,
    private val competitionDao: CompetitionDao
) : FootballRepository {

    override suspend fun getAreasFromApi(): Result<List<Area>> {
        return try {
            val response = apiService.getAreas()

            if (response.isSuccessful) {
                val areas = response.body()?.areas ?: emptyList()
                areaDao.insertAll(areas)
                Result.Success(areas)
            } else {
                Result.Error("Server error: ${response.code()}")
            }

        } catch (e: Exception) {
            Result.Error("Network error: ${e.message ?: "Unknown"}")
        }
    }


    override suspend fun getCachedAreas(): List<Area> {
        return areaDao.getAll()
    }




    override suspend fun getCompetitionsByAreaFromAPi(areaId: Int): Result<List<Competition>> {
        return try {
            val response = apiService.getCompetitionsByArea(areaId)
            val competitions = response.competitions

            competitionDao.insertAll(
                competitions.map {
                    Competition(
                        id = it.id,
                        name = it.name,
                        code = it.code,
                        type = it.type,
                        emblem = it.emblem,
                        plan = it.plan,
                        numberOfAvailableSeasons = it.numberOfAvailableSeasons,
                        lastUpdated = it.lastUpdated,
                        areaId = it.area.id,
                        area = it.area
                    )
                }
            )

            Result.Success(competitions)
        } catch (e: Exception) {
            val fallback = getCachedCompetitions(areaId)
            if (fallback.isNotEmpty()) {
                Result.Success(fallback)
            } else {
                Result.Error("No internet and no cached data")
            }
        }
    }

    override suspend fun getCachedCompetitions(areaId: Int): List<Competition> {
        val cached = competitionDao.getByArea(areaId)
        val area = areaDao.getById(areaId) ?: return emptyList()

        return cached.map {
            Competition(
                id = it.id,
                name = it.name,
                code = it.code,
                type = it.type,
                emblem = it.emblem,
                plan = it.plan,
                numberOfAvailableSeasons = it.numberOfAvailableSeasons,
                lastUpdated = it.lastUpdated,
                area = area,
                areaId = area.id
            )
        }
    }


}

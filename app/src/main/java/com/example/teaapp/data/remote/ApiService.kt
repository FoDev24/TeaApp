package com.example.teaapp.data.remote

import com.example.teaapp.domain.model.AreaResponse
import com.example.teaapp.domain.model.CompetitionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("areas")
    suspend fun getAreas(): Response<AreaResponse>


    @GET("competitions")
    suspend fun getCompetitionsByArea(
        @Query("areas") areaId: Int
    ): CompetitionsResponse

}
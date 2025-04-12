package com.example.teaapp.data.remote

import android.util.Log
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.AreaResponse
import com.example.teaapp.domain.model.CompetitionsResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class FakeApiService : ApiService {
    override suspend fun getAreas(): Response<AreaResponse> {
        return Response.success(
            AreaResponse(
                areas = listOf(
                    Area(
                        id = 1,
                        name = "API Egypt",
                        countryCode = "EG",
                        code = null,
                        flag = null,
                        parentAreaId = null,
                        parentArea = "AF"
                    )
                )
            )
        )
    }


    override suspend fun getCompetitionsByArea(areaId: Int): CompetitionsResponse {
        return CompetitionsResponse(emptyList())
    }
}

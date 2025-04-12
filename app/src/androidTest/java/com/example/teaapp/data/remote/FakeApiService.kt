package com.example.teaapp.data.remote

import android.util.Log
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.AreaResponse
import com.example.teaapp.domain.model.CompetitionsResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class FakeApiService(
    private val areasResponse: suspend () -> Response<AreaResponse> = {
        Response.success(AreaResponse(emptyList()))
    },
    private val competitionsResponse: suspend (Int) -> CompetitionsResponse = {
        CompetitionsResponse(emptyList())
    }
) : ApiService {

    override suspend fun getAreas(): Response<AreaResponse> {
        return areasResponse()
    }

    override suspend fun getCompetitionsByArea(areaId: Int): CompetitionsResponse {
        return competitionsResponse(areaId)
    }
}

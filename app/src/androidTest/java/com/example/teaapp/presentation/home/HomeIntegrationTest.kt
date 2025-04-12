package com.example.teaapp.presentation.home

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.teaapp.data.local.AppDatabase
import com.example.teaapp.data.local.AreaDao
import com.example.teaapp.data.local.CompetitionDao
import com.example.teaapp.data.remote.ApiService
import com.example.teaapp.data.remote.FakeApiService
import com.example.teaapp.data.repository.FootballRepositoryImpl
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.AreaResponse
import com.example.teaapp.domain.model.Competition
import com.example.teaapp.domain.model.CompetitionsResponse
import com.example.teaapp.domain.repository.FootballRepository
import com.example.teaapp.rules.MainDispatcherRule
import com.example.teaapp.test_util.waitForState
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class HomeIntegrationTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)
    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Inject lateinit var areaDao: AreaDao
    @Inject lateinit var competitionDao: CompetitionDao
    private lateinit var viewModel: HomeVm

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun loadAreas_updatesStateWithApiData_onSuccess() = runTest {
        val expectedArea = Area(
            id = 1,
            name = "API Egypt",
            countryCode = "EG",
            code = null,
            flag = null,
            parentAreaId = null,
            parentArea = "AF"
        )

        val fakeApi = FakeApiService(
            areasResponse = {
                Response.success(AreaResponse(listOf(expectedArea)))
            }
        )

        val repo = FootballRepositoryImpl(fakeApi, areaDao, competitionDao)
        viewModel = HomeVm(repo)

        viewModel.onEvent(HomeEvent.LoadAreas)

        val state = waitForState(viewModel.state) { it.areas.isNotEmpty() }

        assertThat(state.areas).containsExactly(expectedArea)
        assertThat(state.isLoading).isFalse()
        assertThat(state.isOffline).isFalse()
        assertThat(state.error).isNull()
    }
    @Test
    fun loadCompetitions_updatesStateWithApiData_onSuccess() = runTest {
        val testArea = Area(
            id = 1, name = "API Egypt", countryCode = "EG",
            code = null, flag = null, parentAreaId = null, parentArea = "AF"
        )

        val expectedCompetition = Competition(
            id = 100,
            name = "Test League",
            code = "TL",
            type = "LEAGUE",
            emblem = null,
            plan = "TIER_ONE",
            numberOfAvailableSeasons = 5,
            lastUpdated = "2024-01-01",
            area = testArea,
            areaId = testArea.id,
            currentSeason = null
        )

        val fakeApi = FakeApiService(
            competitionsResponse = { areaId ->
                CompetitionsResponse(listOf(expectedCompetition))
            }
        )

        val repo = FootballRepositoryImpl(fakeApi, areaDao, competitionDao)
        val viewModel = HomeVm(repo)

        viewModel.onEvent(HomeEvent.LoadCompetitions(testArea.id))

        val state = waitForState(viewModel.state) {
            it.competitionsByArea[testArea.id]?.isNotEmpty() == true
        }

        assertThat(state.competitionsByArea[testArea.id]).containsExactly(expectedCompetition)
        assertThat(state.error).isNull()
    }
    @Test
    fun loadAreas_fallsBackToCache_whenApiFails() = runTest {
        val cachedArea = Area(
            id = 1,
            name = "Cached Egypt",
            countryCode = "EG",
            code = null,
            flag = null,
            parentAreaId = null,
            parentArea = "AF"
        )
        areaDao.insertAll(listOf(cachedArea))

        val fakeApi = FakeApiService(
            areasResponse = {
                throw IOException("Simulated network failure")
            }
        )

        val repo = FootballRepositoryImpl(fakeApi, areaDao, competitionDao)
        viewModel = HomeVm(repo)

        viewModel.onEvent(HomeEvent.LoadAreas)

        val state = waitForState(viewModel.state) { it.areas.isNotEmpty() }

        assertThat(state.areas).containsExactly(cachedArea)
        assertThat(state.error).contains("cached")
        assertThat(state.isOffline).isTrue()
    }

    @Test
    fun loadAreas_showsError_whenApiAndCacheFail() = runTest {
        val fakeApi = FakeApiService(
            areasResponse = {
                throw IOException("API Down")
            }
        )

        val repo = FootballRepositoryImpl(fakeApi, areaDao, competitionDao)
        viewModel = HomeVm(repo)

        viewModel.onEvent(HomeEvent.OnNetworkChanged(false))
        viewModel.onEvent(HomeEvent.LoadAreas)

        val state = waitForState(viewModel.state) { it.error!=null }

        assertThat(state.areas).isEmpty()
        assertThat(state.error).contains("No internet")
        assertThat(state.isOffline).isTrue()
    }

    @Test
    fun loadCompetitions_showsError_whenApiAndCacheFail() = runTest {
        val areaId = 1

        // Simulate API failure
        val fakeApi = FakeApiService(
            competitionsResponse = {
                throw IOException("API failure")
            }
        )

        val repo = FootballRepositoryImpl(fakeApi, areaDao, competitionDao)
        viewModel = HomeVm(repo)

        // Simulate offline
        viewModel.onEvent(HomeEvent.OnNetworkChanged(false))

        // Trigger competition load
        viewModel.onEvent(HomeEvent.LoadCompetitions(areaId))

        val state = waitForState(viewModel.state) { it.error != null }

        // Assert fallback failure
        assertThat(state.competitionsByArea[areaId]).isNull()
        assertThat(state.error).contains("No internet and no cached competitions")
        assertThat(state.isOffline).isTrue()
    }


}

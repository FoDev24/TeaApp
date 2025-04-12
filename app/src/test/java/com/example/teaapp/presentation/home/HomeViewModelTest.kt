package com.example.teaapp.presentation.home

import com.example.teaapp.rules.MainCoroutineRule
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.repository.FootballRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import com.example.teaapp.data.util.Result
import com.example.teaapp.domain.model.Competition
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private val fakeRepo = mock<FootballRepository>()
    private lateinit var viewModel: HomeVm

    @Before
    fun setup() {
        viewModel = HomeVm(fakeRepo)
    }

    @Test
    fun `loadAreas returns correct data`() = runTest {
        val testData = listOf(
            Area(
                id = 1,
                name = "Egypt",
                code = "EG",
                flag = null,
                parentAreaId = 0,
                parentArea = null,
                countryCode = "02"
            )
        )
        whenever(fakeRepo.getAreasFromApi()).thenReturn(Result.Success(testData))

        viewModel.onEvent(HomeEvent.LoadAreas)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.areas).isEqualTo(testData)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `loadCompetitions uses cached data on API failure`() = runTest {
        val areaId = 1

        val testArea = Area(
            id = areaId,
            name = "Egypt",
            code = "EG",
            flag = null,
            parentAreaId = 0,
            parentArea = null,
            countryCode = "02"
        )

        val cachedCompetitions = listOf(
            Competition(
                id = 100,
                name = "Cached League",
                code = "CL",
                type = "LEAGUE",
                emblem = null,
                plan = "TIER_ONE",
                numberOfAvailableSeasons = 3,
                lastUpdated = "2024-01-01",
                area = testArea,
                currentSeason = null,
                areaId = areaId
            )
        )

        // Arrange: Mock behavior
        whenever(fakeRepo.getCompetitionsByAreaFromAPi(areaId)).thenReturn(Result.Error("API failed"))
        whenever(fakeRepo.getCachedCompetitions(areaId)).thenReturn(cachedCompetitions)

        val viewModel = HomeVm(fakeRepo)

        // Act
        viewModel.onEvent(HomeEvent.LoadCompetitions(areaId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertThat(state.competitionsByArea[areaId]).isEqualTo(cachedCompetitions)
        assertThat(state.error).contains("Showing cached competitions")
        assertThat(state.isOffline).isTrue()
    }

    @Test
    fun `loadAreas loads cached data on API failure`() = runTest {
        val testArea = Area(
            id = 1,
            name = "Egypt",
            code = "EG",
            flag = null,
            parentAreaId = 0,
            parentArea = null,
            countryCode = "02"
        )
        val cachedAreas = listOf(testArea)

        // Arrange
        whenever(fakeRepo.getAreasFromApi()).thenReturn(Result.Error("API failed"))
        whenever(fakeRepo.getCachedAreas()).thenReturn(cachedAreas)

        val viewModel = HomeVm(fakeRepo)

        // Act
        viewModel.onEvent(HomeEvent.LoadAreas)
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertThat(state.areas).isEqualTo(cachedAreas)
        assertThat(state.isOffline).isTrue()
        assertThat(state.error).contains("Showing cached data due to")
    }

    @Test
    fun `loadAreas shows error if both API and cache fail`() = runTest {
        whenever(fakeRepo.getAreasFromApi()).thenReturn(Result.Error("Network failure"))
        whenever(fakeRepo.getCachedAreas()).thenReturn(emptyList())

        val viewModel = HomeVm(fakeRepo)

        viewModel.onEvent(HomeEvent.LoadAreas)
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertThat(state.areas).isEmpty()
        assertThat(state.isOffline).isTrue()
        assertThat(state.error).isEqualTo("No internet and no cached data")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `loadCompetitions shows error when both API and cache fail`() = runTest {
        val areaId = 1

        whenever(fakeRepo.getCompetitionsByAreaFromAPi(areaId)).thenReturn(Result.Error("No internet"))
        whenever(fakeRepo.getCachedCompetitions(areaId)).thenReturn(emptyList())

        val viewModel = HomeVm(fakeRepo)

        viewModel.onEvent(HomeEvent.LoadCompetitions(areaId))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.competitionsByArea[areaId]).isNull()
        assertThat(state.error).isEqualTo("No internet and no cached competitions available.")
        assertThat(state.isOffline).isTrue()
    }




}

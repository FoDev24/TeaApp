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

    @Inject lateinit var repository: FootballRepository
    @Inject lateinit var fakeApi: FakeApiService
    private lateinit var viewModel: HomeVm

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = HomeVm(repository)
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

        viewModel.onEvent(HomeEvent.LoadAreas)

        val state = waitForState(viewModel.state) { it.areas.isNotEmpty() }

        assertThat(state.areas).containsExactly(expectedArea)
        assertThat(state.isLoading).isFalse()
        assertThat(state.isOffline).isFalse()
        assertThat(state.error).isNull()
    }




}

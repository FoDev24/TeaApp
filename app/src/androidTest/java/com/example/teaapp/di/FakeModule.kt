package com.example.teaapp.di

import android.content.Context
import androidx.room.Room
import com.example.teaapp.data.local.AppDatabase
import com.example.teaapp.data.local.AreaDao
import com.example.teaapp.data.local.CompetitionDao
import com.example.teaapp.data.remote.ApiService
import com.example.teaapp.data.remote.FakeApiService
import com.example.teaapp.data.repository.FootballRepositoryImpl
import com.example.teaapp.domain.repository.FootballRepository
import com.example.teaapp.presentation.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

// In main/di/AppModule.kt
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeModule {

    @Provides
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideAreaDao(db: AppDatabase): AreaDao = db.areaDao()

    @Provides
    fun provideCompetitionDao(db: AppDatabase): CompetitionDao = db.competitionDao()

    @Provides
    fun provideFakeApiService(): FakeApiService = FakeApiService()

    @Provides
    fun provideFakeRepo(
        api: FakeApiService,
        areaDao: AreaDao,
        competitionDao: CompetitionDao
    ): FootballRepository = FootballRepositoryImpl(api, areaDao, competitionDao)
}

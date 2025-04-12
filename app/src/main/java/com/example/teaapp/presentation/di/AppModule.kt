package com.example.teaapp.presentation.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.teaapp.data.local.AppDatabase
import com.example.teaapp.data.local.AreaDao
import com.example.teaapp.data.local.CompetitionDao
import com.example.teaapp.data.remote.ApiService
import com.example.teaapp.data.repository.FootballRepositoryImpl
import com.example.teaapp.domain.repository.FootballRepository
import com.example.teaapp.data.util.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-level dependencies.
 *
 * This module is installed in the [SingletonComponent], ensuring single instances
 * of provided objects throughout the application lifecycle.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.football-data.org/v4/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideAreaRepository(apiService: ApiService,areaDao: AreaDao , competitionDao: CompetitionDao): FootballRepository {
        return FootballRepositoryImpl(apiService, areaDao =areaDao , competitionDao = competitionDao )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideAreaDao(db: AppDatabase): AreaDao = db.areaDao()

    @Provides
    fun provideCompetitionDao(db: AppDatabase): CompetitionDao = db.competitionDao()


}
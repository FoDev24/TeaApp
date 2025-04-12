package com.example.teaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.Competition

@Database(
    entities = [Competition::class, Area::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao
    abstract fun competitionDao(): CompetitionDao
}

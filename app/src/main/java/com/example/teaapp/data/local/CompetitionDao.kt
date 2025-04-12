package com.example.teaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teaapp.domain.model.Competition

@Dao
interface CompetitionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(competitions: List<Competition>)

    @Query("SELECT * FROM competitions WHERE areaId = :areaId")
    suspend fun getByArea(areaId: Int): List<Competition>
}

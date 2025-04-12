package com.example.teaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teaapp.domain.model.Area

@Dao
interface AreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(areas: List<Area>)

    @Query("SELECT * FROM areas")
    suspend fun getAll(): List<Area>

    @Query("SELECT * FROM areas WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Area?
}

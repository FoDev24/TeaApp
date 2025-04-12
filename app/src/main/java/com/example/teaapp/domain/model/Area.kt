package com.example.teaapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "areas")
data class Area(
    @PrimaryKey val id: Int,
    val name: String?,
    val countryCode: String?,
    val code: String?,
    val flag: String?,
    val parentAreaId: Int?,
    val parentArea: String?
)
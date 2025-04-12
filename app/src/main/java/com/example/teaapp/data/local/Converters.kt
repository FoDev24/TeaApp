package com.example.teaapp.data.local

import androidx.room.TypeConverter
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.CurrentSeason
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromArea(area: Area): String = gson.toJson(area)

    @TypeConverter
    fun toArea(areaString: String): Area {
        val type = object : TypeToken<Area>() {}.type
        return gson.fromJson(areaString, type)
    }

    @TypeConverter
    fun fromCurrentSeason(season: CurrentSeason?): String? {
        return gson.toJson(season)
    }

    @TypeConverter
    fun toCurrentSeason(seasonString: String?): CurrentSeason? {
        if (seasonString == null) return null
        val type = object : TypeToken<CurrentSeason>() {}.type
        return gson.fromJson(seasonString, type)
    }
}

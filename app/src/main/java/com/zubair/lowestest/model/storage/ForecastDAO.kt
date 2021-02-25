package com.zubair.lowestest.model.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ForecastDAO {
    @Insert
    suspend fun insertAll(forecasts: ArrayList<Forecast>): List<Long>

    @Query("SELECT * FROM forecast")
    suspend fun getAllForecasts(): List<Forecast>

    @Query("SELECT * FROM forecast WHERE uuid = :forecastId")
    suspend fun getForecastById(forecastId: String): Forecast

    @Query("DELETE FROM forecast")
    suspend fun deleteAllForecasts()
}
package com.zubair.lowestest.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class Forecast(
    @ColumnInfo(name = "temperature")
    val temperature: String?,

    @ColumnInfo(name = "feels_like")
    val feelsLike: String?,

    @ColumnInfo(name = "weather")
    val weather: String?,

    @ColumnInfo(name = "weather_description")
    val description: String?,

    @ColumnInfo(name = "humidity")
    val humidity: String?,

    @ColumnInfo(name = "wind_speed")
    val wind_speed: String?,

    @ColumnInfo(name = "wind_direction")
    val wind_direction: String?,

    @ColumnInfo(name = "icon_code")
    val iconCode: String?
    ) {
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0
}
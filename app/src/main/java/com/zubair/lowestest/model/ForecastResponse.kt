package com.zubair.lowestest.model

import com.google.gson.annotations.SerializedName
import com.zubair.lowestest.model.storage.Forecast

data class ForecastList(@SerializedName("list") val forecast: List<WeatherResponse>)

data class WeatherResponse (
    @SerializedName("weather") val weatherList: List<Weather>,
    @SerializedName("main") val mainInformation: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("rain") val rain: Rain,
    @SerializedName("snow") val snow: Snow,
    @SerializedName("clouds") val cloud: Cloud,
    @SerializedName("name") val city: String,
    @SerializedName("dt_txt") val dateTime: String
)

data class Weather(
    @SerializedName("id") val id: Int? = -1,
    @SerializedName("main") val main: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)

data class Main(
    @SerializedName("temp") val currentTemperature: Double?,
    @SerializedName("temp_min") val minTemperature: Double?,
    @SerializedName("temp_max") val maxTemperature: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("pressure") val pressure: Double?,
    @SerializedName("humidity") val humidity: Int?
)

data class Wind(
    @SerializedName("speed") val speed: Double?,
    @SerializedName("deg") val direction: Double?
)

data class Cloud(@SerializedName("all") val coverage: Int?)

data class Rain(
    @SerializedName("1h") val oneHour: Double?,
    @SerializedName("3h") val direction: Double?
)

data class Snow(
    @SerializedName("1h") val oneHour: Double?,
    @SerializedName("3h") val direction: Double?
)

sealed class ResponseType{
    object Error: ResponseType()
    data class Success(val forecastList: List<Forecast>): ResponseType()
}




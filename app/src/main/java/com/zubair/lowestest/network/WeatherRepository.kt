package com.zubair.lowestest.network

import android.content.SharedPreferences
import androidx.core.content.edit
import com.zubair.lowestest.model.ResponseType
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.model.storage.ForecastDatabase
import com.zubair.lowestest.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val sharedPreferences: SharedPreferences,
    private val database: ForecastDatabase
) {

    suspend fun getForecasts(city: String): ResponseType {
        val lastSavedCity = sharedPreferences.getString(Constants.LAST_SAVED_CITY, "")
        if (lastSavedCity == city) {
            return ResponseType.Success(database.getForecastDAO().getAllForecasts())
        } else {
            database.getForecastDAO().deleteAllForecasts()
            val response = weatherApi.getCurrentWeather(city, Constants.UNIT, Constants.API_KEY)
            if (response.isSuccessful) {
                sharedPreferences.edit {
                    putString(Constants.LAST_SAVED_CITY, city)
                }
                val responseBody = response.body()
                val forecastList = ArrayList<Forecast>()
                responseBody?.let { list ->
                    list.forecast.forEach {
                        forecastList.add(
                            Forecast(
                                it.mainInformation?.currentTemperature?.toString(),
                                it.mainInformation?.feelsLike?.toString(),
                                it.weatherList?.first().main,
                                it.weatherList?.first().description,
                                it.mainInformation?.humidity?.toString(),
                                "${it.wind?.speed} MPH",
                                convertDegreeToCardinalDirection(it.wind?.direction),
                                it.weatherList.first().icon
                            )
                        )
                    }

                    val result = database.getForecastDAO().insertAll(forecastList)
                    var index = 0
                    while (index < forecastList.size) {
                        forecastList[index].uuid = result[index].toInt()
                        ++index
                    }
                }

                return ResponseType.Success(forecastList)

            } else {
                return ResponseType.Error(response.message(), response.code())
            }
        }

    }

    private fun convertDegreeToCardinalDirection(directionInDegrees: Double?): String {
        var cardinalDirection = ""
        if (directionInDegrees != null) {
            cardinalDirection = if (directionInDegrees in 348.75..360.0 ||
                directionInDegrees in 0.0..11.25
            ) {
                "N"
            } else if (directionInDegrees in 11.25..33.75) {
                "NNE"
            } else if (directionInDegrees in 33.75..56.25) {
                "NE"
            } else if (directionInDegrees in 56.25..78.75) {
                "ENE"
            } else if (directionInDegrees in 78.75..101.25) {
                "E"
            } else if (directionInDegrees in 101.25..123.75) {
                "ESE"
            } else if (directionInDegrees in 123.75..146.25) {
                "SE"
            } else if (directionInDegrees in 146.25..168.75) {
                "SSE"
            } else if (directionInDegrees in 168.75..191.25) {
                "S"
            } else if (directionInDegrees in 191.25..213.75) {
                "SSW"
            } else if (directionInDegrees in 213.75..236.25) {
                "SW"
            } else if (directionInDegrees in 236.25..258.75) {
                "WSW"
            } else if (directionInDegrees in 258.75..281.25) {
                "W"
            } else if (directionInDegrees in 281.25..303.75) {
                "WNW"
            } else if (directionInDegrees in 303.75..326.25) {
                "NW"
            } else if (directionInDegrees in 326.25..348.75) {
                "NNW"
            } else {
                "?"
            }
        }

        return cardinalDirection
    }
}
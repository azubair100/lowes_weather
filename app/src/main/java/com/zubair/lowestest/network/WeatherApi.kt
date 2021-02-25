package com.zubair.lowestest.network

import com.zubair.lowestest.model.ForecastList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") unit: String,
        @Query("appid") key: String
        ): Response<ForecastList>

}
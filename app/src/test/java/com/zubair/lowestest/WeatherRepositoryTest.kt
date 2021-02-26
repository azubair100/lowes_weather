package com.zubair.lowestest

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zubair.lowestest.model.*
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.model.storage.ForecastDatabase
import com.zubair.lowestest.network.WeatherApi
import com.zubair.lowestest.network.WeatherRepository
import com.zubair.lowestest.util.Constants
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var weatherApi: WeatherApi

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    @MockK
    lateinit var database: ForecastDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        weatherApi = mockk<WeatherApi>(relaxed = true)
        sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        database = mockk<ForecastDatabase>(relaxed = true)
    }


    @After
    fun cleanUp() {
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getForecastPreviousCity() {
        val repo = WeatherRepository(weatherApi, sharedPreferences, database)
        val forecastListFromDB = arrayListOf(
            Forecast("45", "47", "cloud", "broken clouds", "344", "50mph", "NE", "10S"),
            Forecast("48", "57", "clear", "clear sky", "1044", "5mph", "N", "1S")
        )

        every { sharedPreferences.getString(Constants.LAST_SAVED_CITY, "") } returns "New York"
        coEvery { database.getForecastDAO().getAllForecasts() } returns forecastListFromDB

        runBlocking {
            val getListWhenCityWasSaved = repo.getForecasts("New York")
            assertEquals(forecastListFromDB, getListWhenCityWasSaved)
        }
    }

    @Test
    fun getForecastFromApiSuccess() {
        val repo = WeatherRepository(weatherApi, sharedPreferences, database)
        val forecastResponse = ForecastList(
            arrayListOf(
                WeatherResponse(
                    weatherList = arrayListOf(
                        Weather(
                            id = 800,
                            main = "Clear",
                            description = "clear sky",
                            icon = "01n"
                        )
                    ),
                    mainInformation = Main(
                        currentTemperature = 69.91,
                        minTemperature = 66.25,
                        maxTemperature = 69.91,
                        feelsLike = 67.96,
                        pressure = 1006.0,
                        humidity = 68
                    ),
                    wind = Wind(speed = 8.52, direction = 184.0),
                    rain = null,
                    snow = null,
                    cloud = Cloud(coverage = 0), city = null,
                    dateTime = "2021-02-26 00:00:00"
                )
            )
        )
        val successfulResponse = Response.success(forecastResponse)
        every { sharedPreferences.getString(Constants.LAST_SAVED_CITY, "") } returns "New York"
        coEvery {
            weatherApi.getCurrentWeather(
                "Dallas",
                "imperial",
                Constants.API_KEY
            )
        } returns successfulResponse
        runBlocking {
            val response = repo.getForecasts("Dallas")
            assertEquals(
                (successfulResponse.body() as List<*>).size,
                (response as ResponseType.Success).forecastList?.size
            )
        }

    }

    @Test
    fun getForecastFromApiFaiure() {
        val repo = WeatherRepository(weatherApi, sharedPreferences, database)
        val forecastResponse = ForecastList(
            arrayListOf(
                WeatherResponse(
                    weatherList = arrayListOf(
                        Weather(
                            id = 800,
                            main = "Clear",
                            description = "clear sky",
                            icon = "01n"
                        )
                    ),
                    mainInformation = Main(
                        currentTemperature = 69.91,
                        minTemperature = 66.25,
                        maxTemperature = 69.91,
                        feelsLike = 67.96,
                        pressure = 1006.0,
                        humidity = 68
                    ),
                    wind = Wind(speed = 8.52, direction = 184.0),
                    rain = null,
                    snow = null,
                    cloud = Cloud(coverage = 0), city = null,
                    dateTime = "2021-02-26 00:00:00"
                )
            )
        )

        val errorResponse = Response.error<ForecastList>(500, null)
        every { sharedPreferences.getString(Constants.LAST_SAVED_CITY, "") } returns "New York"
        coEvery {
            weatherApi.getCurrentWeather(
                "Dallas",
                "imperial",
                Constants.API_KEY
            )
        } returns errorResponse
        runBlocking {
            val response = repo.getForecasts("Dallas")
            assertEquals(
                errorResponse.code(),
                (response as ResponseType.Error).errorCode
            )
        }

    }

}
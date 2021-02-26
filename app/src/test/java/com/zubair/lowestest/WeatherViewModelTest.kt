package com.zubair.lowestest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zubair.lowestest.model.ResponseType
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.network.WeatherRepository
import com.zubair.lowestest.util.NetworkMonitor
import com.zubair.lowestest.viewmodel.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var weatherRepository: WeatherRepository

    @MockK
    lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        weatherRepository = mockk<WeatherRepository>(relaxed = true)
        networkMonitor = mockk<NetworkMonitor>(relaxed = true)
    }

    @After
    fun cleanUp() {
        dispatcher.cleanupTestCoroutines()
    }


    @Test
    fun getForecastListSuccessNull() {
        val responseSuccessEmpty = ResponseType.Success(null)
        coEvery { networkMonitor.hasInternetFlow } returns flow{ true }
        coEvery { weatherRepository.getForecasts("new york") } returns responseSuccessEmpty

        val viewModel = WeatherViewModel(weatherRepository, networkMonitor)
        viewModel.getForecast("new york")
        assertEquals(responseSuccessEmpty.forecastList, viewModel.forecastList.value)
    }

    @Test
    fun getForecastListSuccess() {
        val forecastList = arrayListOf(
            Forecast("45", "47", "cloud", "broken clouds", "344", "50mph", "NE", "10S"),
            Forecast("48", "57", "clear", "clear sky", "1044", "5mph", "N", "1S")
        )

        val responseSuccessNonEmpty = ResponseType.Success(forecastList)
        coEvery { networkMonitor.hasInternetFlow } returns flow{ true }
        coEvery { weatherRepository.getForecasts("new york") } returns responseSuccessNonEmpty

        val viewModel = WeatherViewModel(weatherRepository, networkMonitor)
        viewModel.getForecast("new york")
        assertEquals(responseSuccessNonEmpty.forecastList?.size, viewModel.forecastList.value?.size)
    }
}
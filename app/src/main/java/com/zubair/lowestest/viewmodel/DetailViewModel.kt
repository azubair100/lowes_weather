package com.zubair.lowestest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.model.storage.ForecastDatabase
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val database: ForecastDatabase
) : ViewModel() {

    private val forecast = MutableLiveData<Forecast>()
    val forecastLiveData: LiveData<Forecast> get() = forecast

    fun getDetails(uuid: String) {
        viewModelScope.launch {
            forecast.postValue(database.getForecastDAO().getForecastById(uuid))
        }
    }
}
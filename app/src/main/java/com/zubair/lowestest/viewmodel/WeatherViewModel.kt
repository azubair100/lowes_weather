package com.zubair.lowestest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zubair.lowestest.model.ResponseType
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.network.WeatherRepository
import com.zubair.lowestest.util.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val forecastLiveData = MutableLiveData<List<Forecast>>()
    val forecastList: LiveData<List<Forecast>> get() = forecastLiveData

    private val requestStateMutable = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> get() = requestStateMutable

    fun getForecast(cityName: String) {
        try {
            viewModelScope.launch {
                networkMonitor.hasInternetFlow.collect { hasInternetConnection ->
                    if (hasInternetConnection) {
                        when (val response = weatherRepository.getForecasts(cityName)) {
                            is ResponseType.Error -> {
                                Log.e("Error", "error ${response.errorMessage}")
                                requestStateMutable.postValue(RequestState.RequestFailedGeneral)
                            }
                            is ResponseType.Success -> {
                                val list = response.forecastList
                                if (list.isNullOrEmpty()) {
                                    requestStateMutable.postValue(RequestState.EmptyResult)
                                } else {
                                    requestStateMutable.postValue(RequestState.NonEmptyResult)
                                    forecastLiveData.postValue(list)
                                }
                            }
                        }
                    } else {
                        requestStateMutable.postValue(RequestState.RequestFailedConnectivity)
                    }
                }


            }
        } catch (error: Exception) {
            requestStateMutable.postValue(RequestState.RequestFailedGeneral)
        }

    }

    sealed class RequestState {
        object CurrentlyLoading : RequestState()
        object NonEmptyResult : RequestState()
        object EmptyResult : RequestState()
        object RequestFailedConnectivity : RequestState()
        object RequestFailedGeneral : RequestState()
    }


}
package com.zubair.lowestest.util

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Singleton

@Singleton
class NetworkMonitor(context: Application) {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    @ExperimentalCoroutinesApi
    val hasInternetFlow: Flow<Boolean>
        @RequiresApi(Build.VERSION_CODES.N)
        get() {
            return callbackFlow {
                offer(hasInternet)
                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        val connected =
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        offer(connected)
                    }

                    override fun onLost(network: Network) {
                        offer(false)
                    }
                }
                connectivityManager?.registerDefaultNetworkCallback(callback)
                awaitClose {
                    connectivityManager?.unregisterNetworkCallback(callback)
                }
            }.distinctUntilChanged()
        }

    private val hasInternet: Boolean
        get() = connectivityManager?.let { cm ->
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false

}
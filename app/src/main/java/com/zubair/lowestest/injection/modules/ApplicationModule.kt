package com.zubair.lowestest.injection.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.zubair.lowestest.model.storage.ForecastDatabase
import com.zubair.lowestest.util.Constants
import com.zubair.lowestest.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {
    //TODO add all DB (Room), DAOs / Repos, Service (Retrofit) provides here
    @Singleton
    @Provides
    fun provideViewModelFactory(application: Application): ViewModelProvider.AndroidViewModelFactory =
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)

    @Singleton
    @Provides
    fun provideSharedPreference(application: Application): SharedPreferences =
        application.getSharedPreferences(Constants.PREF_KEY, Constants.PRIVATE_MODE)


    @Singleton
    @Provides
    fun provideForecastDAO(application: Application) = ForecastDatabase(application)

    @Singleton
    @Provides
    fun provideNetworkMonitor(application: Application) = NetworkMonitor(application)
}
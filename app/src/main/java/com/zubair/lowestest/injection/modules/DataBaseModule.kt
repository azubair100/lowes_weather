package com.zubair.lowestest.injection.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Database

@Module
class DataBaseModule {
    /*@Singleton
    @Database
    @Provides
    fun provideDatabase(app: Application) = WeatherDatabase(app)*/
}
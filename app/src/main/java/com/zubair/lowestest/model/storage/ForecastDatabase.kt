package com.zubair.lowestest.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Forecast::class], version = 1)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun getForecastDAO(): ForecastDAO

    companion object{
        @Volatile private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{ instance = it}
        }

        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext, ForecastDatabase::class.java, "forecast_database"
        ).build()
    }
}
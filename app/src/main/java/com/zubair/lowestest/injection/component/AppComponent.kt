package com.zubair.lowestest.injection.component

import android.app.Application
import com.zubair.lowestest.App
import com.zubair.lowestest.injection.modules.ActivityModule
import com.zubair.lowestest.injection.modules.ApplicationModule
import com.zubair.lowestest.injection.modules.DataBaseModule
import com.zubair.lowestest.injection.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        NetworkModule::class,
        DataBaseModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
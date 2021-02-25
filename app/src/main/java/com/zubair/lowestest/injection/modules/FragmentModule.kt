package com.zubair.lowestest.injection.modules

import com.zubair.lowestest.view.CityNameFragment
import com.zubair.lowestest.view.DetailFragment
import com.zubair.lowestest.view.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCityNameFragment() : CityNameFragment

    @ContributesAndroidInjector
    abstract fun contributeListFragment() : ListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment() : DetailFragment
}
package com.chaitanya.android.delivery.dagger.injector

import com.chaitanya.android.delivery.view.map.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidMapInjectBuilder {

    @ContributesAndroidInjector
    abstract fun bindMapActivity():
            MapActivity
}
package com.chaitanya.android.delivery.dagger.module

import android.content.Context

import com.chaitanya.android.delivery.MyApp


import javax.inject.Singleton

import dagger.Module
import dagger.Provides


@Module
class ApplicationModule {

    @Singleton
    @Provides
    internal fun provideContext(application: MyApp): Context {
        return application
    }
}

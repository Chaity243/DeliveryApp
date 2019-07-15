package com.chaitanya.android.delivery.dagger.component

import com.chaitanya.android.delivery.MyApp
import com.chaitanya.android.delivery.dagger.injector.AndroidDeliveryInjectBuilder
import com.chaitanya.android.delivery.dagger.injector.AndroidMapInjectBuilder
import com.chaitanya.android.delivery.dagger.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,AndroidDeliveryInjectBuilder::class, AndroidMapInjectBuilder::class,DeliveryActivityModule::class, ApplicationModule::class, NetworkModule::class,ApplicationModule::class, DatabaseModule::class, RepositoryModule::class])
interface DeliveryMainComponent : AndroidInjector<MyApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApp>()

}
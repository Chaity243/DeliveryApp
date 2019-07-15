package com.chaitanya.android.delivery.dagger.injector

import com.chaitanya.android.delivery.view.delivery.DeliveryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidDeliveryInjectBuilder {


    @ContributesAndroidInjector
    abstract fun bindDeliveryActivity():
            DeliveryActivity

}
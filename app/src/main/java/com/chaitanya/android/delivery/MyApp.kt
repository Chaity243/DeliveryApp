package com.chaitanya.android.delivery


import com.chaitanya.android.delivery.dagger.component.DaggerDeliveryMainComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

 class MyApp : DaggerApplication() {
     override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
         return DaggerDeliveryMainComponent.builder().create(this)
     }





}
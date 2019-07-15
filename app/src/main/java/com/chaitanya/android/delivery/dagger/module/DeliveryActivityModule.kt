package com.chaitanya.android.delivery.dagger.module

import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.chaitanya.android.delivery.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
 class DeliveryActivityModule {
    @Provides
    @Named("DeliveryActivity")
    fun provideViewModelFactory(deliveryRepository: IDeliveryRepository): ViewModelFactory {
        return ViewModelFactory(deliveryRepository)
    }


}
package com.chaitanya.android.delivery.dagger.module


import com.chaitanya.android.delivery.model.mapper.DeliveryMapper
import com.chaitanya.android.delivery.pagination.DeliveryDataSource
import com.chaitanya.android.delivery.model.repository.DeliveryRepository
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.chaitanya.android.delivery.model.repository.network.DeliveryAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDeliveryRepository(
        deliveryAPI: DeliveryAPI,
        deliveryMapper: DeliveryMapper,
        deliveryDataSource: DeliveryDataSource
    ): IDeliveryRepository {
        return DeliveryRepository(deliveryAPI, deliveryMapper, deliveryDataSource)
    }
}
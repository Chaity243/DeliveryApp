package com.chaitanya.android.delivery.dagger.module

import android.arch.persistence.room.Room
import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.MyApp
import com.chaitanya.android.delivery.model.repository.databse.DeliveryDao
import com.chaitanya.android.delivery.model.repository.databse.DeliveryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule{

    @Singleton
    @Provides
    internal fun providesDeliveryDatabase(mApplication: MyApp): DeliveryDatabase {
        return Room.databaseBuilder(mApplication, DeliveryDatabase::class.java, BuildConfig.DATABASE_NAME).build()
    }


    @Singleton
    @Provides
    internal fun providesDeliveryDao(deliveryDatabase: DeliveryDatabase): DeliveryDao {
        return deliveryDatabase.deliveryDao()
    }


}
package com.chaitanya.android.delivery.model.repository.databse

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.chaitanya.android.delivery.util.Constants
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery

@Database(entities = [(Delivery::class)], version = Constants.DB_VERSION, exportSchema = false)
abstract class DeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
}
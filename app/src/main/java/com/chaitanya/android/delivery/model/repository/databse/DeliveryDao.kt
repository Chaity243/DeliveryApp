package com.chaitanya.android.delivery.model.repository.databse

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import io.reactivex.Single

@Dao
interface DeliveryDao {

    @Query("SELECT * FROM DELIVERY ORDER BY id ASC")
    fun getAllDeliveries(): DataSource.Factory<Int, Delivery>

    @Query("SELECT * FROM DELIVERY WHERE id= :deliveryId")
    fun getDeliveryAgainstId(deliveryId: Int): Single<Delivery>

    @Query("SELECT count(*) FROM DELIVERY")
    fun getDeliveryItemsCount(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleDeliveryItem(delivery: Delivery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDeliveryItems(deliveries: List<Delivery>)

    @Query("DELETE FROM DELIVERY")
    fun clearAll()
}
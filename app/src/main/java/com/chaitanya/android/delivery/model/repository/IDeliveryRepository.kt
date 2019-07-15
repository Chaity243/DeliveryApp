package com.chaitanya.android.delivery.model.repository

import android.arch.paging.DataSource
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import io.reactivex.Single

interface IDeliveryRepository {
    fun getDeliveries(offset: Int, limit: Int): Single<List<Delivery>>

    fun getDeliveries(): DataSource.Factory<Int, Delivery>

    fun addToLocal(deliveries: List<Delivery>)

    fun getDelivery(id: Int): Single<Delivery>

    fun getCount(): Single<Int>

    fun removeAll()
}
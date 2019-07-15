package com.chaitanya.android.delivery.pagination

import com.chaitanya.android.delivery.model.repository.databse.DeliveryDao
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import io.reactivex.Single
import javax.inject.Inject

class DeliveryDataSource @Inject constructor(private var deliveryDao: DeliveryDao) {

    fun getAllDeliveries() = deliveryDao.getAllDeliveries()

    fun insertIntoLocal(deliveries: List<Delivery>) = deliveryDao.insertAllDeliveryItems(deliveries)

    fun getDeliveryAgainstId(id: Int) = deliveryDao.getDeliveryAgainstId(id)

    fun getDeliveryItemsCount(): Single<Int> = deliveryDao.getDeliveryItemsCount()

    fun clearAll() = deliveryDao.clearAll()
}